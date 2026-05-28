package com.petshop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Credenciais de login")
public class LoginRequest {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Schema(example = "joao@email.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(example = "senha123")
    private String password;
}
