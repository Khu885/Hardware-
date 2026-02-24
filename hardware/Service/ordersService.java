package com.hardwareaplications.hardware.Service;

import com.hardwareaplications.hardware.HardwaresRepo.orderRepo;
import com.hardwareaplications.hardware.orders;
import com.hardwareaplications.hardware.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service

public class ordersService {

    @Autowired
    private orderRepo repo;

    public List<orders> getorders() {
        return repo.findAll();
    }

    public orders getorderbyID(int oId) {
        return repo.findById(oId).orElse(null);
    }

    public orders addorder(orders o) {

        return repo.save(o);
    }


    public orders updateorders(orders o) {
        return repo.save(o);
    }

    public String deleteorder(int oId) {
        repo.deleteById(oId);
        return "Order of id : "+oId+ "Deleted";
    }

    public void Load() {
        List<orders> ordersList = new ArrayList<>((Arrays.asList(
                new orders(1, 1, "2023-01-01", 100.0, 2),
                new orders(2, 2, "2023-02-01", 250.5, 5)
        )));
        repo.saveAll(ordersList);
    }
}
