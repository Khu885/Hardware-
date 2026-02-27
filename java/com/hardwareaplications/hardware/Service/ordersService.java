package com.hardwareaplications.hardware.Service;

import com.hardwareaplications.hardware.Exception.*;
import com.hardwareaplications.hardware.HardwaresRepo.InventoryRepo;
import com.hardwareaplications.hardware.HardwaresRepo.orderRepo;
import com.hardwareaplications.hardware.HardwaresRepo.ProductRepo;
import com.hardwareaplications.hardware.Inventory;
import com.hardwareaplications.hardware.orders;
import com.hardwareaplications.hardware.product;
import com.hardwareaplications.hardware.customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class ordersService {

    @Autowired
    private orderRepo repo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ProductRepo productRepo; // to fetch product price

    @Autowired
    private customerservice customerService; // to update customer due_amt

    private void validateOrder(orders o, boolean isUpdate) {
        if (o == null) {
            throw new OrderValidationException("Order must not be null");
        }
        if (isUpdate) {
            if (!repo.existsById(o.getOrderId())) {
                throw new OrderValidationException("Order id must exist for update");
            }
        }
        // Validate Customer existence
        try {
            customerService.getcustomerbyid(o.getCustomerId());
        } catch (CustomerNotFoundException e) {
            throw new OrderValidationException("Customer with ID " + o.getCustomerId() + " does not exist");
        }

        // Validate Product existence
        if (!productRepo.existsById(o.getProductId())) {
            throw new OrderValidationException("Product with ID " + o.getProductId() + " does not exist");
        }

        if (o.getQuantity() <= 0) {
            throw new OrderValidationException("Order quantity must be positive");
        }
        if (o.getOrderDate() == null || o.getOrderDate().isBlank()) {
            throw new OrderValidationException("Order date must not be blank");
        }
    }

    public List<orders> getorders() {
        return repo.findAll();
    }

    public orders getorderbyID(int oId) {
        return repo.findById(oId).orElse(null);
    }

    public orders addorder(orders o) {
        validateOrder(o, false);
        // calculate totalAmount based on product price and quantity before saving
        o.setTotalAmount(calculateTotalAmount(o));
        if (o.isProcessed()) {
            processOrder(o);
            // Increase customer's due amount when order is processed
            try {
                customer c = customerService.getcustomerbyid(o.getCustomerId());
                if (c != null) {
                    int currentDue = c.getDue_amt();
                    int addAmt = (int) Math.round(o.getTotalAmount());
                    c.setDue_amt(currentDue + addAmt);
                    customerService.updatecustomer(c);
                }
            } catch (Exception ex) {
                // swallow or log; prefer fail-fast in real app
                throw new RuntimeException("Failed to update customer due amount: " + ex.getMessage());
            }
        }
        return repo.save(o);
    }


    public orders updateorders(orders o) {
        validateOrder(o, true);
        Optional<orders> existingOrder = repo.findById(o.getOrderId());
        // Always recalculate total amount to avoid stale values
        o.setTotalAmount(calculateTotalAmount(o));
        if (o.isProcessed()) {
            // Only process if this order wasn't previously processed (or it's a new order)
            if (existingOrder.isEmpty() || !existingOrder.get().isProcessed()) {
                processOrder(o);
                // increase customer due_amt accordingly
                customer c = customerService.getcustomerbyid(o.getCustomerId());
                if (c != null) {
                    int currentDue = c.getDue_amt();
                    int addAmt = (int) Math.round(o.getTotalAmount());
                    c.setDue_amt(currentDue + addAmt);
                    customerService.updatecustomer(c);
                }
            }
        }
        return repo.save(o);
    }

    private double calculateTotalAmount(orders o) {
        if (o == null) return 0.0;
        product p = productRepo.findById(o.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product with id " + o.getProductId() + " not found"));
        if (p.getPrice() == null) {
            throw new RuntimeException("Product price is not set for product id: " + o.getProductId());
        }
        if (o.getQuantity() <= 0) {
            throw new RuntimeException("Order quantity must be positive");
        }
        return p.getPrice() * o.getQuantity();
    }

    private void processOrder(orders o) {
        List<Inventory> inventories = inventoryRepo.findByProductId(o.getProductId());
        if (!inventories.isEmpty()) {
            // Assume we take from the first inventory location available
            Inventory inventory = inventories.get(0);
            int newQuantity = inventory.getQuantity() - o.getQuantity();
            if (newQuantity < 0) {
                 throw new InventoryValidationException("Insufficient inventory for product ID: " + o.getProductId());
            }
            inventory.setQuantity(newQuantity);
            inventoryRepo.save(inventory);
        } else {
             throw new InventoryNotFoundException("No inventory found for product ID: " + o.getProductId());
        }
    }

    /**
     * Return (cancel) an order: restore inventory, decrement customer's due_amt, and mark order as unprocessed.
     */
    public orders returnOrder(int oId) {
        orders o = repo.findById(oId).orElseThrow(() -> new OrderNotFoundException("Order with id " + oId + " not found"));
        if (!o.isProcessed()) {
            throw new OrderValidationException("Order with id " + oId + " is not processed or already returned");
        }
        // Restore inventory: add back quantity to first inventory for this product
        List<Inventory> inventories = inventoryRepo.findByProductId(o.getProductId());
        if (inventories.isEmpty()) {
            throw new InventoryNotFoundException("No inventory found for product ID: " + o.getProductId() + " to restore");
        }
        Inventory inventory = inventories.get(0);
        inventory.setQuantity(inventory.getQuantity() + o.getQuantity());
        inventoryRepo.save(inventory);

        // Update customer's due amount (subtract order total)
        try {
            customer c = customerService.getcustomerbyid(o.getCustomerId());
            int currentDue = c.getDue_amt();
            int reduceAmt = (int) Math.round(o.getTotalAmount());
            int newDue = currentDue - reduceAmt;
            if (newDue < 0) newDue = 0; // prevent negative due
            c.setDue_amt(newDue);
            customerService.updatecustomer(c);
        } catch (CustomerNotFoundException e) {
             throw new CustomerNotFoundException("Customer with id " + o.getCustomerId() + " not found while returning order");
        }

        // Mark order as not processed to prevent double return
        o.setProcessed(false);
        return repo.save(o);
    }

    public String deleteorder(int oId) {
        repo.deleteById(oId);
        return "Order of id : "+oId+ "Deleted";
    }

    public void Load() {
        List<orders> ordersList = new ArrayList<>((Arrays.asList(
                // use null or 0 price by default; these will get recalculated when saving if products exist
                new orders(1, 1, 1, "2023-01-01", 0.0, 2, false),
                new orders(2, 2, 2, "2023-02-01", 0.0, 5, false)
        )));
        // Ensure totalAmount is calculated for each order before save
        for (orders o : ordersList) {
            o.setTotalAmount(calculateTotalAmount(o));
        }
        repo.saveAll(ordersList);
    }
}
