package com.playground.ecommerce.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("100.00");
    private static final BigDecimal STANDARD_SHIPPING_FEE = new BigDecimal("10.00");

    public BigDecimal calculateTax(BigDecimal subtotal) {
        return subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateShipping(BigDecimal subtotal) {
        if (subtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return STANDARD_SHIPPING_FEE;
    }

    public BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal tax, BigDecimal shipping) {
        return subtotal.add(tax).add(shipping).setScale(2, RoundingMode.HALF_UP);
    }
}
