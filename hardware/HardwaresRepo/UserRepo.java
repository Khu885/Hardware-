package com.hardwareaplications.hardware.HardwaresRepo;

import com.hardwareaplications.hardware.AdmUsers;
import com.hardwareaplications.hardware.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AdmUsers, Integer> {
    AdmUsers findByUsername(String username);
}
