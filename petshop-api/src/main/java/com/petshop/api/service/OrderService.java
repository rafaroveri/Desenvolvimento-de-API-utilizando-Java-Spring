package com.petshop.api.service;

import com.petshop.api.dto.request.OrderItemRequest;
import com.petshop.api.dto.request.OrderRequest;
import com.petshop.api.dto.request.OrderStatusRequest;
import com.petshop.api.dto.response.OrderItemResponse;
import com.petshop.api.dto.response.OrderResponse;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.*;
import com.petshop.api.repository.OrderRepository;
import com.petshop.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAllWithDetails().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findByCustomerId(Long customerId) {
        customerService.findCustomerById(customerId);
        return orderRepository.findByCustomerIdWithDetails(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        Customer customer = customerService.findCustomerById(request.getCustomerId());

        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + itemReq.getProductId()));

            if (product.getStock() < itemReq.getQuantity()) {
                throw new BusinessException("Estoque insuficiente para '" + product.getName() +
                        "'. Disponível: " + product.getStock() + ", solicitado: " + itemReq.getQuantity());
            }

            BigDecimal unitPrice = product.getPrice();

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(unitPrice)
                    .build();

            order.getItems().add(item);
            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);

            total = total.add(unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }

        order.setTotalAmount(total);
        return toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Não é possível alterar o status de um pedido cancelado.");
        }

        if (request.getStatus() == OrderStatus.CANCELLED) {
            restoreStock(order);
        }

        order.setStatus(request.getStatus());
        return toResponse(orderRepository.save(order));
    }

    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));

        if (order.getStatus() != OrderStatus.CANCELLED) {
            restoreStock(order);
        }
        orderRepository.delete(order);
    }

    private void restoreStock(Order order) {
        if (order.getItems() == null) return;
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }
}
