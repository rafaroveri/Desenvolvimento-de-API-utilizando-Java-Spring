package com.petshop.api.service;

import com.petshop.api.dto.request.AddressRequest;
import com.petshop.api.dto.request.CustomerRequest;
import com.petshop.api.dto.response.AddressResponse;
import com.petshop.api.dto.response.CustomerResponse;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.Address;
import com.petshop.api.model.Customer;
import com.petshop.api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return toResponse(findCustomerById(id));
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        // Regra: email e CPF únicos
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um cliente com o email: " + request.getEmail());
        }
        if (customerRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe um cliente com o CPF: " + request.getCpf());
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .cpf(request.getCpf())
                .phone(request.getPhone())
                .address(toAddressModel(request.getAddress()))
                .build();

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = findCustomerById(id);

        // Verifica unicidade excluindo o próprio registro
        if (customerRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new BusinessException("Já existe um cliente com o email: " + request.getEmail());
        }
        if (customerRepository.existsByCpfAndIdNot(request.getCpf(), id)) {
            throw new BusinessException("Já existe um cliente com o CPF: " + request.getCpf());
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setCpf(request.getCpf());
        customer.setPhone(request.getPhone());
        customer.setAddress(toAddressModel(request.getAddress()));

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = findCustomerById(id);
        customerRepository.delete(customer);
    }

    // ── Métodos auxiliares ──────────────────────────────────────────────────────

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
    }

    private Address toAddressModel(AddressRequest req) {
        if (req == null) return null;
        return Address.builder()
                .street(req.getStreet())
                .number(req.getNumber())
                .neighborhood(req.getNeighborhood())
                .city(req.getCity())
                .state(req.getState())
                .zipCode(req.getZipCode())
                .build();
    }

    private AddressResponse toAddressResponse(Address address) {
        if (address == null) return null;
        return AddressResponse.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .cpf(customer.getCpf())
                .phone(customer.getPhone())
                .address(toAddressResponse(customer.getAddress()))
                .build();
    }
}
