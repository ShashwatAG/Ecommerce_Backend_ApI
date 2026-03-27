package com.playground.ecommerce.dto.order;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CheckoutRequest {

    @NotBlank(message = "Shipping address is required.")
    @Size(max = 2000, message = "Shipping address must be at most 2000 characters.")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required.")
    @Size(max = 100, message = "Payment method must be at most 100 characters.")
    private String paymentMethod;

    @Size(max = 1000, message = "Notes must be at most 1000 characters.")
    private String notes;

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

