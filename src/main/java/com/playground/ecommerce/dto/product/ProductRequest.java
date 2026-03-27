package com.playground.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Product name is required.")
    @Size(max = 255, message = "Product name must be at most 255 characters.")
    private String name;

    @NotBlank(message = "SKU is required.")
    @Size(max = 100, message = "SKU must be at most 100 characters.")
    private String sku;

    @Size(max = 2000, message = "Description must be at most 2000 characters.")
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required.")
    @Min(value = 0, message = "Stock quantity cannot be negative.")
    private Integer stockQuantity;

    @Size(max = 1000, message = "Image URL must be at most 1000 characters.")
    private String imageUrl;

    @NotNull(message = "Category id is required.")
    private Long categoryId;

    private Boolean active = Boolean.TRUE;
}
