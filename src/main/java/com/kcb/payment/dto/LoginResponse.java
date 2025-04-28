package com.kcb.payment.dto;

import com.kcb.payment.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String jwtToken;
    private User user;
}
