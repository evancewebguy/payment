package com.kcb.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusRequest {
    private String endToEndId;
    private String phone;
}
