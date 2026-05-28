package com.petshop.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(name = "street", length = 300)
    private String street;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "neighborhood", length = 150)
    private String neighborhood;

    @Column(name = "city", length = 150)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zip_code", length = 9)
    private String zipCode;
}
