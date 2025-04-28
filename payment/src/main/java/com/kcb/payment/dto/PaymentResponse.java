package com.kcb.payment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String transactionId;
    private String endToEndId;
    private PaymentStatus status;
}
