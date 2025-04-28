package com.kcb.payment.services;

import com.kcb.payment.dto.*;

public interface AuthenticationService {
    /**
     * Creates a new user account based on the provided registration details.
     *
     * @param payload Registration request containing user information
     * @return BaseResponse containing the created User object or error details
     * @throws IllegalArgumentException if the registration data is invalid
     */
    BaseResponse<String> signUp(BaseRequest<RegisterUser> payload);

    /**
     * Authenticates an existing user and returns their details.
     *
     * @param payload Login request containing authentication credentials
     * @return BaseResponse containing the authenticated User object or error details
     * @throws IllegalArgumentException if the login credentials are invalid
     */
    BaseResponse<LoginResponse> login(BaseRequest<LoginUser> payload);
}