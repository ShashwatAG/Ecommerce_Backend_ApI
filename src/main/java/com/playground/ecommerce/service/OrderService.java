package com.playground.ecommerce.service;

import com.playground.ecommerce.dto.order.AdminUpdateOrderRequest;
import com.playground.ecommerce.dto.order.CheckoutRequest;
import com.playground.ecommerce.dto.order.OrderItemResponse;
import com.playground.ecommerce.dto.order.OrderResponse;
import com.playground.ecommerce.exception.BadRequestException;
import com.playground.ecommerce.exception.ResourceNotFoundException;
import com.playground.ecommerce.model.Cart;
import com.playground.ecommerce.model.CartItem;
import com.playground.ecommerce.model.Order;
import com.playground.ecommerce.model.OrderItem;
import com.playground.ecommerce.model.OrderStatus;
import com.playground.ecommerce.model.PaymentStatus;
import com.playground.ecommerce.model.Product;
import com.playground.ecommerce.model.User;
import com.playground.ecommerce.repository.CartRepository;
import com.playground.ecommerce.repository.OrderRepository;
import com.playground.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final CartService cartService;
    private final PricingService pricingService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CartRepository cartRepository,
                        AuthenticatedUserService authenticatedUserService,
                        CartService cartService,
                        PricingService pricingService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.cartService = cartService;
        this.pricingService = pricingService;
    }

    @Transactional
    public OrderResponse checkout(String email, CheckoutRequest request) {
        User user = authenticatedUserService.getByEmail(email);
        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty.");
        }

        BigDecimal subtotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (!product.isActive()) {
                throw new BadRequestException(product.getName() + " is no longer available.");
            }
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            subtotal = subtotal.add(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        BigDecimal tax = pricingService.calculateTax(subtotal);
        BigDecimal shipping = pricingService.calculateShipping(subtotal);
        BigDecimal total = pricingService.calculateTotal(subtotal, tax, shipping);

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod().trim());
        order.setShippingAddress(request.getShippingAddress().trim());
        order.setNotes(request.getNotes());
        order.setSubtotal(subtotal);
        order.setTaxAmount(tax);
        order.setShippingFee(shipping);
        order.setTotalAmount(total);

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductSku(product.getSku());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setLineTotal(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);
        cart.clearItems();
        cartRepository.save(cart);
        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String email) {
        User user = authenticatedUserService.getByEmail(email);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getMyOrder(String email, Long orderId) {
        User user = authenticatedUserService.getByEmail(email);
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, AdminUpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }
        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }

        return toResponse(orderRepository.save(order));
    }

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setCustomerId(order.getUser().getId());
        response.setCustomerName(order.getUser().getFullName());
        response.setCustomerEmail(order.getUser().getEmail());
        response.setStatus(order.getStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setShippingAddress(order.getShippingAddress());
        response.setNotes(order.getNotes());
        response.setSubtotal(order.getSubtotal());
        response.setTaxAmount(order.getTaxAmount());
        response.setShippingFee(order.getShippingFee());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> items = new ArrayList<OrderItemResponse>();
        for (OrderItem orderItem : order.getItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductId(orderItem.getProductId());
            itemResponse.setProductName(orderItem.getProductName());
            itemResponse.setProductSku(orderItem.getProductSku());
            itemResponse.setQuantity(orderItem.getQuantity());
            itemResponse.setUnitPrice(orderItem.getUnitPrice());
            itemResponse.setLineTotal(orderItem.getLineTotal());
            items.add(itemResponse);
        }
        response.setItems(items);
        return response;
    }

    private String generateOrderNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + datePart + "-" + randomPart;
    }
}
