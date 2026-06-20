package com.savpoint.savpoint.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank (message = "O email do usuário não pode estar vazio.")
        String email,

        @NotBlank (message = "A senha do usuário não pode estar vazia")
        String password
) {
}
