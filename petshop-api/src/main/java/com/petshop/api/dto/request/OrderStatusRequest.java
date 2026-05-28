package com.petshop.api.dto.request;

import com.petshop.api.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Atualização de status do pedido")
public class OrderStatusRequest {

    @NotNull(message = "O status é obrigatório")
    @Schema(description = "Novo status do pedido", example = "CONFIRMED")
    private OrderStatus status;
}
