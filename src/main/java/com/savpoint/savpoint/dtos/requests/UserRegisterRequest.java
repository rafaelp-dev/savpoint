package com.savpoint.savpoint.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(
        @NotBlank (message = "O nome do usuário não pode estar vazio.")
        String username,

        @NotBlank (message = "O email do usuário não pode estar vazio.")
        String email,

        @NotBlank (message = "A senha do usuário não pode estar vazia.")
        String password
) {
}
