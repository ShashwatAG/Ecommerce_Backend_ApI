package com.playground.ecommerce.dto.order;

import com.playground.ecommerce.model.OrderStatus;
import com.playground.ecommerce.model.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateOrderRequest {

    private OrderStatus status;
    private PaymentStatus paymentStatus;
}
