package com.rosenberg.uni.Entities;

import java.util.HashMap;
import java.util.Map;

public class Car implements Entity {
    private String id;
    private String make;
    private String model;
    private String ownerID;

    public Car(String make, String model, String ownerID) {
        this.make = make;
        this.model = model;
        this.ownerID = ownerID;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("make", make);
        map.put("model", model);
        map.put("ownerID", ownerID);
        return map;
    }
}
