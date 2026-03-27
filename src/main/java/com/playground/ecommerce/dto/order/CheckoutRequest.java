package com.playground.ecommerce.dto.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CheckoutRequest {

    @NotBlank(message = "Shipping address is required.")
    @Size(max = 2000, message = "Shipping address must be at most 2000 characters.")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required.")
    @Size(max = 100, message = "Payment method must be at most 100 characters.")
    private String paymentMethod;

    @Size(max = 1000, message = "Notes must be at most 1000 characters.")
    private String notes;
}
