package com.playground.ecommerce.controller;

import com.playground.ecommerce.dto.user.UserProfileResponse;
import com.playground.ecommerce.service.AuthenticatedUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthenticatedUserService authenticatedUserService;

    public UserController(AuthenticatedUserService authenticatedUserService) {
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/me")
    public UserProfileResponse getProfile(Authentication authentication) {
        return UserProfileResponse.from(authenticatedUserService.getByEmail(authentication.getName()));
    }
}

