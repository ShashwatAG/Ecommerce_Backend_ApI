package com.playground.ecommerce.service;

import com.playground.ecommerce.dto.cart.AddCartItemRequest;
import com.playground.ecommerce.dto.cart.CartItemResponse;
import com.playground.ecommerce.dto.cart.CartResponse;
import com.playground.ecommerce.dto.cart.UpdateCartItemRequest;
import com.playground.ecommerce.exception.BadRequestException;
import com.playground.ecommerce.exception.ResourceNotFoundException;
import com.playground.ecommerce.model.Cart;
import com.playground.ecommerce.model.CartItem;
import com.playground.ecommerce.model.Product;
import com.playground.ecommerce.model.User;
import com.playground.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final ProductService productService;

    public CartService(CartRepository cartRepository,
                       AuthenticatedUserService authenticatedUserService,
                       ProductService productService) {
        this.cartRepository = cartRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.productService = productService;
    }

    @Transactional
    public CartResponse getCart(String email) {
        User user = authenticatedUserService.getByEmail(email);
        return toResponse(getOrCreateCart(user));
    }

    @Transactional
    public CartResponse addItem(String email, AddCartItemRequest request) {
        User user = authenticatedUserService.getByEmail(email);
        Cart cart = getOrCreateCart(user);
        Product product = productService.findProduct(request.getProductId());

        if (!product.isActive()) {
            throw new BadRequestException("This product is currently unavailable.");
        }

        CartItem existingItem = findCartItemByProductId(cart, product.getId());
        int finalQuantity = request.getQuantity();
        if (existingItem != null) {
            finalQuantity = existingItem.getQuantity() + request.getQuantity();
        }

        validateStock(product, finalQuantity);

        if (existingItem != null) {
            existingItem.setQuantity(finalQuantity);
            existingItem.setUnitPrice(product.getPrice());
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            item.setUnitPrice(product.getPrice());
            cart.addItem(item);
        }

        return toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse updateItem(String email, Long itemId, UpdateCartItemRequest request) {
        User user = authenticatedUserService.getByEmail(email);
        Cart cart = getOrCreateCart(user);
        CartItem item = findCartItemById(cart, itemId);

        validateStock(item.getProduct(), request.getQuantity());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(item.getProduct().getPrice());

        return toResponse(cartRepository.save(cart));
    }

    @Transactional
    public void removeItem(String email, Long itemId) {
        User user = authenticatedUserService.getByEmail(email);
        Cart cart = getOrCreateCart(user);
        cart.removeItem(findCartItemById(cart, itemId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(String email) {
        User user = authenticatedUserService.getByEmail(email);
        Cart cart = getOrCreateCart(user);
        cart.clearItems();
        cartRepository.save(cart);
    }

    @Transactional
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    user.setCart(cart);
                    return cartRepository.save(cart);
                });
    }

    public CartResponse toResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setUpdatedAt(cart.getUpdatedAt());

        List<CartItemResponse> items = new ArrayList<CartItemResponse>();
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItem item : cart.getItems()) {
            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setSku(item.getProduct().getSku());
            itemResponse.setImageUrl(item.getProduct().getImageUrl());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setAvailableStock(item.getProduct().getStockQuantity());
            itemResponse.setUnitPrice(item.getUnitPrice());

            BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            itemResponse.setLineTotal(lineTotal);
            items.add(itemResponse);
            subtotal = subtotal.add(lineTotal);
            totalItems += item.getQuantity();
        }

        response.setItems(items);
        response.setSubtotal(subtotal);
        response.setTotalItems(totalItems);
        return response;
    }

    private void validateStock(Product product, int quantity) {
        if (product.getStockQuantity() < quantity) {
            throw new BadRequestException("Only " + product.getStockQuantity() + " items available for " + product.getName() + ".");
        }
    }

    private CartItem findCartItemById(Cart cart, Long itemId) {
        for (CartItem item : cart.getItems()) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        throw new ResourceNotFoundException("Cart item not found.");
    }

    private CartItem findCartItemByProductId(Cart cart, Long productId) {
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
    }
}
