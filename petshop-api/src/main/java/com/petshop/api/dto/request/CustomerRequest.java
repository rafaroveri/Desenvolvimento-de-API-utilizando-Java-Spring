package com.petshop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Dados para criação ou atualização de cliente")
public class CustomerRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 200)
    @Schema(example = "João Silva")
    private String name;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 200)
    @Schema(example = "joao.silva@email.com")
    private String email;

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00")
    @Schema(example = "123.456.789-00")
    private String cpf;

    @Pattern(regexp = "\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (00) 00000-0000")
    @Schema(example = "(11) 99999-8888")
    private String phone;

    @Valid
    @Schema(description = "Endereço do cliente")
    private AddressRequest address;
}
