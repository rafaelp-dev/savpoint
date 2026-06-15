package com.savpoint.savpoint.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(
        @NotNull (message = "O email do usuário não pode estar vazio.")
        String email,

        @NotNull (message = "A senha do usuário não pode estar vazia")
        String password
) {
}
