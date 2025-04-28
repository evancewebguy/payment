package com.kcb.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentInitiationRequest {
    private String phoneNumber;
    private BigDecimal amount;
    private String provider;
    private String endToEndId;
}
