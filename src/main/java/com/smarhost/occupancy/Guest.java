package com.smarhost.occupancy;

public class Guest {
    static public String getCategory(Double budget){
        return budget >= 100 ? "premium": "economy";
    }
}
