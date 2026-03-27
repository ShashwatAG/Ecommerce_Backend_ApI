package com.playground.ecommerce.controller;

import com.playground.ecommerce.dto.cart.AddCartItemRequest;
import com.playground.ecommerce.dto.cart.CartResponse;
import com.playground.ecommerce.dto.cart.UpdateCartItemRequest;
import com.playground.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(Authentication authentication) {
        return cartService.getCart(authentication.getName());
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(Authentication authentication, @Valid @RequestBody AddCartItemRequest request) {
        return cartService.addItem(authentication.getName(), request);
    }

    @PutMapping("/items/{itemId}")
    public CartResponse updateItem(Authentication authentication,
                                   @PathVariable Long itemId,
                                   @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateItem(authentication.getName(), itemId, request);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(Authentication authentication, @PathVariable Long itemId) {
        cartService.removeItem(authentication.getName(), itemId);
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
    }
}
