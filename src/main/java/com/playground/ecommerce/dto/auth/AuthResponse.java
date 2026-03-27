package com.playground.ecommerce.dto.auth;

import com.playground.ecommerce.dto.user.UserProfileResponse;

public class AuthResponse {

    private String token;
    private String tokenType;
    private UserProfileResponse user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserProfileResponse getUser() {
        return user;
    }

    public void setUser(UserProfileResponse user) {
        this.user = user;
    }
}

