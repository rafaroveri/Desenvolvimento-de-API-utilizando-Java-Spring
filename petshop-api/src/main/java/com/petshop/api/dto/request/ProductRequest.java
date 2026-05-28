package com.petshop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Dados para criação ou atualização de produto")
public class ProductRequest {

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    @Schema(description = "Nome do produto", example = "Ração Premium Golden Adulto 15kg")
    private String name;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    @Schema(description = "Descrição detalhada do produto")
    private String description;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    @Schema(description = "Preço do produto", example = "149.90")
    private BigDecimal price;

    @NotNull(message = "O estoque é obrigatório")
    @PositiveOrZero(message = "O estoque não pode ser negativo")
    @Schema(description = "Quantidade em estoque", example = "50")
    private Integer stock;

    @Size(max = 500, message = "A URL da imagem deve ter no máximo 500 caracteres")
    @Schema(description = "URL da imagem do produto")
    private String imageUrl;

    @NotNull(message = "A categoria é obrigatória")
    @Schema(description = "ID da categoria do produto", example = "1")
    private Long categoryId;
}
