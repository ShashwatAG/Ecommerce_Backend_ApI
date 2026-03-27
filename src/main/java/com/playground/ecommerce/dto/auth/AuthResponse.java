package com.playground.ecommerce.dto.auth;

import com.playground.ecommerce.dto.user.UserProfileResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String token;
    private String tokenType;
    private UserProfileResponse user;
}
