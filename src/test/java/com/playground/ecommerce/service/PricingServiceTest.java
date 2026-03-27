package com.playground.ecommerce.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest {

    private final PricingService pricingService = new PricingService();

    @Test
    void shouldApplyStandardShippingBelowThreshold() {
        assertEquals(new BigDecimal("10.00"), pricingService.calculateShipping(new BigDecimal("75.00")));
    }

    @Test
    void shouldApplyFreeShippingAtThreshold() {
        assertEquals(new BigDecimal("0.00"), pricingService.calculateShipping(new BigDecimal("100.00")));
    }

    @Test
    void shouldCalculateTaxAndTotal() {
        BigDecimal subtotal = new BigDecimal("50.00");
        BigDecimal tax = pricingService.calculateTax(subtotal);
        BigDecimal shipping = pricingService.calculateShipping(subtotal);

        assertEquals(new BigDecimal("5.00"), tax);
        assertEquals(new BigDecimal("65.00"), pricingService.calculateTotal(subtotal, tax, shipping));
    }
}
