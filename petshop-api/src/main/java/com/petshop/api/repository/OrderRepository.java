package com.petshop.api.repository;

import com.petshop.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.items i JOIN FETCH i.product WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(Long id);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.items i JOIN FETCH i.product")
    List<Order> findAllWithDetails();

    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.items i JOIN FETCH i.product WHERE o.customer.id = :customerId")
    List<Order> findByCustomerIdWithDetails(Long customerId);
}
