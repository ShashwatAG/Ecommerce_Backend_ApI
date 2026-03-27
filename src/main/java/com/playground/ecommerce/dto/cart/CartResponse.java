package com.playground.ecommerce.dto.cart;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResponse {

    private Long id;
    private Long userId;
    private Integer totalItems;
    private BigDecimal subtotal;
    private LocalDateTime updatedAt;
    private List<CartItemResponse> items = new ArrayList<CartItemResponse>();
}
