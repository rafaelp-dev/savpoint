package com.savpoint.savpoint.service.domain;

import com.savpoint.savpoint.dtos.requests.UserProfileRequest;
import com.savpoint.savpoint.dtos.responses.MessageResponse;
import com.savpoint.savpoint.entities.UserEntity;
import com.savpoint.savpoint.entities.UserProfileEntity;
import com.savpoint.savpoint.enums.UserRole;
import com.savpoint.savpoint.exceptions.ConflictException;
import com.savpoint.savpoint.exceptions.NotFoundException;
import com.savpoint.savpoint.repositories.UserProfileRepository;
import com.savpoint.savpoint.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public MessageResponse createProfile (Authentication authentication, UserProfileRequest userProfileRequest) {
        String email = authentication.getName();

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não cadastrado"));

        if (userProfileRepository.existsByDisplayName(userProfileRequest.displayName())) {
            throw new ConflictException("Este nome de usuário já está em uso.");
        }

        UserProfileEntity userProfileEntity = new UserProfileEntity(
                userEntity,
                userProfileRequest.displayName()
        );

        userEntity.setRole(UserRole.DEFAULT);

        userProfileRepository.save(userProfileEntity);

        return new MessageResponse(
                "Perfil cadastrado com sucesso!"
        );
    }
}
