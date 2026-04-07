package com.shirish.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentMethod {
    RAZORPAY,
    STRIPE;

    @JsonCreator
    public static PaymentMethod fromString(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid payment method: " + value);
    }
}
