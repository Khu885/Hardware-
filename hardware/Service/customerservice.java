package com.hardwareaplications.hardware.Service;

import com.hardwareaplications.hardware.customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class customerservice {
    public List<customer> customers= new ArrayList<>(
            Arrays.asList(new customer(1,"raman",1234567890,"xyz",0)));

    public List<customer> getcustomer(){
        return customers;
    }

    public customer getcustomerbyid(int cid) {
        return customers.stream()
                .filter(c -> c.getCustomerId() == cid)
                .findFirst().orElse(null);
    }

    public customer addcustomer(customer cid) {
        customers.add(cid);
        return cid;
    }

    public customer updatecustomer(customer cid) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId() == cid.getCustomerId()) {
                customers.set(i, cid);
                return cid;
            }
        }
        return null;
    }

    public List<customer> deletecustomer(int cid) {
        int index = -1;
        for (int i=0;i<customers.size();i++){
            if(customers.get(i).getCustomerId() == cid){
                index=i;
                break;
            }
        }
        if (index != -1) {
            customers.remove(index);
        }
        return customers;
    }
}
