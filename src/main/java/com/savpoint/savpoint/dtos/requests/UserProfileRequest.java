package com.savpoint.savpoint.dtos.requests;

import com.savpoint.savpoint.entities.UserEntity;

public record UserProfileRequest(
        String displayName
) {
}
