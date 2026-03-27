package com.playground.ecommerce.dto.order;

import com.playground.ecommerce.model.OrderStatus;
import com.playground.ecommerce.model.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String shippingAddress;
    private String notes;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items = new ArrayList<OrderItemResponse>();
}
