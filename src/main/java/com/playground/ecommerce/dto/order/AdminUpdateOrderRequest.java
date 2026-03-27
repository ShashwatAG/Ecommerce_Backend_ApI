package com.playground.ecommerce.dto.order;

import com.playground.ecommerce.model.OrderStatus;
import com.playground.ecommerce.model.PaymentStatus;

public class AdminUpdateOrderRequest {

    private OrderStatus status;
    private PaymentStatus paymentStatus;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
