package com.savpoint.savpoint.controllers;

import com.savpoint.savpoint.dtos.requests.UserProfileRequest;
import com.savpoint.savpoint.dtos.responses.MessageResponse;
import com.savpoint.savpoint.service.domain.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createProfile (Authentication authentication, @RequestBody @Valid UserProfileRequest userProfileRequest) {
        MessageResponse messageResponse = userProfileService.createProfile(authentication, userProfileRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }
}
