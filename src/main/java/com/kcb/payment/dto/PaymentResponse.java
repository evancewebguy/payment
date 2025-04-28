package com.kcb.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private String transactionId;
    private String endToEndId;
    private PaymentStatus status;
}
