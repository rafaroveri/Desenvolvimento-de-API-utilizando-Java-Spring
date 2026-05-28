package com.petshop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Item de um pedido")
public class OrderItemRequest {

    @NotNull(message = "O ID do produto é obrigatório")
    @Schema(description = "ID do produto", example = "1")
    private Long productId;

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    @Schema(description = "Quantidade do produto", example = "2")
    private Integer quantity;
}
