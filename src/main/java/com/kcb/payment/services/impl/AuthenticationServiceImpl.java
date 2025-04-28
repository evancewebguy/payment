package com.kcb.payment.services.impl;

import com.kcb.payment.domain.User;
import com.kcb.payment.dto.*;
import com.kcb.payment.repositories.UserRepository;
import com.kcb.payment.security.JwtService;
import com.kcb.payment.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public BaseResponse<String> signUp(BaseRequest<RegisterUser> payload) {
        User user = new User();

        user.setFullName(payload.getData().getFullName());
        user.setEmail(payload.getData().getEmail());
        user.setPassword(passwordEncoder.encode(payload.getData().getPassword()));
        userRepository.save(user);
        return BaseResponse.<String>builder()
                .status("success")
                .data("User created successfully")
                .build();
    }

    @Override
    public BaseResponse<LoginResponse> login(BaseRequest<LoginUser> payload) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        payload.getData().getEmail(),
                        payload.getData().getPassword()
                )
        );


        User user = userRepository.findByEmail(payload.getData().getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        LoginResponse response = LoginResponse.builder()
                .user(user)
                .jwtToken(jwtToken)
                .build();

        return BaseResponse.<LoginResponse>builder()
                .status("Success")
                .data(response)
                .build();
    }
}
