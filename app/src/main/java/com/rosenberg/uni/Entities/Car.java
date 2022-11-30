package com.rosenberg.uni.Entities;

import com.google.firebase.firestore.DocumentId;

import java.util.HashMap;
import java.util.Map;

public class Car {
    @DocumentId
    String documentId;

    private String make;
    private String model;
    private String mileage;
    private String ownerID;
    private String numOfSeats;
    private String fuel;
    private String gearbox;
    private String startDate;
    private String endDate;
    private String renterID;

    public String getRenterID() {
        return renterID;
    }

    public Car(){

    }

    public Car(String make, String model, String mileage, String numOfSeats, String fuel, String gearbox, String startDate, String endDate, String ownerID) {
        this.make = make;
        this.model = model;
        this.mileage = mileage;
        this.numOfSeats = numOfSeats;
        this.fuel = fuel;
        this.gearbox = gearbox;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownerID = ownerID;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(String numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setRenterID(String renterID) {
        this.renterID = renterID;
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
}
