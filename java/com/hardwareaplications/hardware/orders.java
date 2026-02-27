package com.hardwareaplications.hardware;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import com.hardwareaplications.hardware.validate.ExistsCustomer;
import com.hardwareaplications.hardware.validate.ExistsProduct;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class orders {
    @Positive(message = "orderId must be a positive number")
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Positive(message = "customerId must be a positive number")
    @ExistsCustomer(message = "Customer with this ID does not exist in the database")
    private int customerId;

    @Positive(message = "productId must be a positive number")
    @ExistsProduct(message = "Product with this ID does not exist in the database")
    private int productId;

    @NotBlank(message = "orderDate must not be blank")
    private String orderDate;

    @PositiveOrZero(message = "totalAmount must be zero or positive")
    private double totalAmount;

    @Positive(message = "Quantity must be a positive number")
    private int Quantity;

    private boolean isProcessed;
}
