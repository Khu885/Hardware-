package com.hardwareaplications.hardware.Controller;

import com.hardwareaplications.hardware.Service.usersService;
import com.hardwareaplications.hardware.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
@RestController
public class usersController {
    @Autowired
    usersService Uservice;

    @GetMapping("/users")
    public List<user> getusers(){
        return Uservice.getusers();
    }

    @GetMapping("/users/{uId}")
    public user getuserbyID(@PathVariable int uId){
        return Uservice.getuserbyID(uId);
    }

    @PostMapping("/users")
    public user addusers(@Valid @RequestBody user u) {
        return Uservice.addusers(u);
    }

    @PutMapping("/users")
    public user updateusers( @Valid @RequestBody user u){
        return Uservice.updateusers(u);
    }

    @DeleteMapping("/users/{uId}")
    public List<user>deleteusers(@PathVariable int uId){
        return Uservice.deleteusers(uId);
    }
}
