package com.smarhost.occupancy;

public class Customer {
    static public String getCategory(Double budget){
        return budget >= 100 ? "premium": "economy";
    }
}
