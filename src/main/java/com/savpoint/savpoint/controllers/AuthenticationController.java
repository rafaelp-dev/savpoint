package com.savpoint.savpoint.controllers;

import com.savpoint.savpoint.dtos.requests.UserLoginRequest;
import com.savpoint.savpoint.dtos.requests.UserRegisterRequest;
import com.savpoint.savpoint.dtos.responses.UserLoginResponse;
import com.savpoint.savpoint.dtos.responses.UserRegisterResponse;
import com.savpoint.savpoint.service.configurations.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register (@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        UserRegisterResponse userRegisterResponse = authenticationService.register(userRegisterRequest);

        return ResponseEntity.ok().body(userRegisterResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login (@RequestBody @Valid UserLoginRequest userLoginRequest) {

    }
}
