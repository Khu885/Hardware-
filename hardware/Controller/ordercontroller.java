package com.hardwareaplications.hardware.Controller;



import com.hardwareaplications.hardware.Service.ordersService;
import com.hardwareaplications.hardware.orders;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class ordercontroller {
    private ordersService service;

//    public ordercontroller(ordersService service) {
//        this.service = service;
//    }

    @GetMapping("/orders")
    public List<orders> getorders() {
        return service.getorders();
    }

    @GetMapping("/orders/{oId}")
    public orders getorderbyID(@PathVariable int oId) {
        return service.getorderbyID(oId);
    }

    @PostMapping("/orders")
    public orders addorder(@Valid @RequestBody orders o) {
        return service.addorder(o);
    }

    @PutMapping("/orders")
    public orders updateorders(@Valid @RequestBody orders o) {
        return service.updateorders(o);
    }

    @DeleteMapping("/orders/{oId}")
    public String deleteorder(@PathVariable int oId) {
        return service.deleteorder(oId);
    }

    @GetMapping("/LoadOrders")
    public String Load() {
        service.Load();
        return "Order Data Loaded";
    }
}