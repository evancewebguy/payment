package com.kcb.payment.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String phone;
    private String email;
    private String message;
}
