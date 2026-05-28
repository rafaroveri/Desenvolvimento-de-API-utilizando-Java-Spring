package com.petshop.api.model;

/**
 * Enum de status do pedido, representando o ciclo de vida de uma Order.
 */
public enum OrderStatus {
    PENDING,    // Aguardando confirmação
    CONFIRMED,  // Confirmado, aguardando envio
    SHIPPED,    // Enviado
    DELIVERED,  // Entregue
    CANCELLED   // Cancelado
}
