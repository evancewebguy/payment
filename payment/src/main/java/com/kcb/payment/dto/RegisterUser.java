package com.kcb.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUser {
    private String email;
    private String password;
    private String fullName;
}
