package com.petshop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Endereço do cliente")
public class AddressRequest {

    @NotBlank(message = "O logradouro é obrigatório")
    @Size(max = 300)
    @Schema(example = "Rua das Flores")
    private String street;

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 20)
    @Schema(example = "123")
    private String number;

    @NotBlank(message = "O bairro é obrigatório")
    @Size(max = 150)
    @Schema(example = "Centro")
    private String neighborhood;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 150)
    @Schema(example = "São Paulo")
    private String city;

    @NotBlank(message = "O estado (UF) é obrigatório")
    @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres (ex: SP)")
    @Schema(example = "SP")
    private String state;

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000")
    @Schema(example = "01001-000")
    private String zipCode;
}
