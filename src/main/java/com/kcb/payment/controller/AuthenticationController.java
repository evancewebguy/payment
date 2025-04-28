package com.kcb.payment.controller;

import com.kcb.payment.dto.BaseRequest;
import com.kcb.payment.dto.BaseResponse;
import com.kcb.payment.dto.LoginUser;
import com.kcb.payment.dto.RegisterUser;
import com.kcb.payment.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<BaseResponse<?>> authenticatedUser(@RequestBody BaseRequest<LoginUser> payload) {
        return ResponseEntity.ok(authenticationService.login(payload));
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<?>> signUp(@RequestBody BaseRequest<RegisterUser> payload) {
        return new ResponseEntity<>(authenticationService.signUp(payload), HttpStatus.CREATED);
    }
}
