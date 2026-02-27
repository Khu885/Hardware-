package com.hardwareaplications.hardware.Controller;


import com.hardwareaplications.hardware.Inventory;
import com.hardwareaplications.hardware.Service.inventoryService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class InventoryController {
    inventoryService service = new inventoryService();

    @GetMapping("/inventories")
    public List<Inventory> getinventories() {
        return service.getinventories();
    }

    @GetMapping("/inventories/{inventoryId}")
    public Inventory getinventorybyID(@PathVariable int inventoryId) {
        return service.getinventorybyID(inventoryId);
    }

    @PostMapping("/inventories")
    public Inventory addinventory(@Valid @RequestBody Inventory i) {
        return service.addinventory(i);
    }

    @PutMapping("/inventories")
    public Inventory updateinventories(@Valid @RequestBody Inventory i) {
        return service.updateinventories(i);
    }

    @DeleteMapping("/inventories/{inventoryId}")
    public String deleteinventory(@PathVariable int inventoryId) {
        return service.deleteinventory(inventoryId);
    }

    @GetMapping("/LoadInventories")
    public String Load() {
        service.Load();
        return "Inventory Data Loaded";
    }
}
