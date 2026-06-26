package com.savpoint.savpoint.service.configurations;

import com.savpoint.savpoint.dtos.requests.UserLoginRequest;
import com.savpoint.savpoint.dtos.requests.UserRegisterRequest;
import com.savpoint.savpoint.dtos.responses.UserLoginResponse;
import com.savpoint.savpoint.dtos.responses.UserRegisterResponse;
import com.savpoint.savpoint.entities.UserEntity;
import com.savpoint.savpoint.enums.UserRole;
import com.savpoint.savpoint.exceptions.ConflictException;
import com.savpoint.savpoint.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public UserRegisterResponse register (UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByEmail(userRegisterRequest.email()).isPresent()) {
            throw new ConflictException("Este email já está cadastrado");
        }

        String encyptedPassword =new BCryptPasswordEncoder().encode(userRegisterRequest.password());

        UserEntity userEntity = new UserEntity(
                userRegisterRequest.username(),
                userRegisterRequest.email(),
                encyptedPassword
        );

        userEntity.setRole(UserRole.REGISTERED);

        userRepository.save(userEntity);

        return new UserRegisterResponse(
                "Usuário registrado com sucesso"
        );
    }

    public UserLoginResponse login (UserLoginRequest userLoginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userLoginRequest.email(), userLoginRequest.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        String token = tokenService.generateToken((UserEntity) authentication.getPrincipal());

        return new UserLoginResponse(token);
    }
}
