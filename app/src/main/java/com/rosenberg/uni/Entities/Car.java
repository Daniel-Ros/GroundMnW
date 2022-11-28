package com.rosenberg.uni.Entities;

import java.util.HashMap;
import java.util.Map;

public class Car implements Entity {
    private String Document_ID;
    private String id;
    private String make;
    private String model;
    private String mileage;
    private String ownerID;

    public Car(){

    }

    public String getDocument_ID() {
        return Document_ID;
    }

    public void setDocument_ID(String document_ID) {
        Document_ID = document_ID;
    }

    public Car(String make, String model, String mileage, String ownerID) {
        this.make = make;
        this.model = model;
        this.mileage = mileage;
        this.ownerID = ownerID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getMileage() {
        return mileage;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("make", make);
        map.put("model", model);
        map.put("mileage", mileage);
        map.put("ownerID", ownerID);
        return map;
    }
}
