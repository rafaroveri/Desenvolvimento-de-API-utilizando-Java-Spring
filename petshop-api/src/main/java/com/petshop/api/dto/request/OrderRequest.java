package com.petshop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Dados para criação de pedido")
public class OrderRequest {

    @NotNull(message = "O ID do cliente é obrigatório")
    @Schema(description = "ID do cliente", example = "1")
    private Long customerId;

    @NotEmpty(message = "O pedido deve ter ao menos um item")
    @Valid
    @Schema(description = "Itens do pedido")
    private List<OrderItemRequest> items;
}
