package com.playground.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {

    private Long id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private boolean active;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
