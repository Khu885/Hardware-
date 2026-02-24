package com.hardwareaplications.hardware.HardwaresRepo;

import com.hardwareaplications.hardware.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
}
