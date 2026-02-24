package com.hardwareaplications.hardware.Service;

import com.hardwareaplications.hardware.HardwaresRepo.InventoryRepo;
import com.hardwareaplications.hardware.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class inventoryService {

    @Autowired
    private InventoryRepo repo;

    public List<Inventory> getinventories() {
        return repo.findAll();
    }

    public Inventory getinventorybyID(int inventoryId) {
        return repo.findById(inventoryId).orElse(null);
    }

    public Inventory addinventory(Inventory inv) {
        return repo.save(inv);
    }

    public Inventory updateinventories(Inventory inv) {
        repo.save(inv);
         return inv;
    }

    public String deleteinventory(int inventoryId) {
        repo.deleteById(inventoryId);
        return "Inventory of id : "+inventoryId+ " is Deleted";
    }

    public void Load() {
        List<Inventory> inventoryList = new ArrayList<>((Arrays.asList(
                new Inventory(1, 1, 10, "Backroom showcase"),
                new Inventory(2, 2, 5, "bike room")
        )));
        repo.saveAll(inventoryList);
    }
}
