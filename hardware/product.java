package com.hardwareaplications.hardware;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class product {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ProductId;
    private String ProductName;
    private String ProductBrand;
    private String ProductColour;
    private String ProductSize;
    private String ProductType;
}
