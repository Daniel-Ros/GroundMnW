package com.rosenberg.uni.Entities;

import com.google.firebase.firestore.DocumentId;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Car {
    @DocumentId
    String documentId;

    private String make;
    private String model;
    private Integer mileage;
    private String ownerID;
    private Integer numOfSeats;
    private String fuel;
    private String gearbox;
    private String startDate;
    private Long startDateStamp;
    private String endDate;
    private Long endDateStamp;
    private String renterID;

    public String getRenterID() {
        return renterID;
    }

    public Car(){

    }

    public Car(String make, String model, String mileage, String numOfSeats, String fuel, String gearbox, String startDate, String endDate, String ownerID) {
        this.make = make;
        this.model = model;
        this.mileage = Integer.parseInt(mileage);
        this.numOfSeats = Integer.parseInt(numOfSeats);
        this.fuel = fuel;
        this.gearbox = gearbox;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownerID = ownerID;

        String [] splitdate = startDate.split("/");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
                Integer.parseInt(splitdate[1]),
                Integer.parseInt(splitdate[0]));
        this.startDateStamp = calendar.getTimeInMillis();

        splitdate = endDate.split("/");
        calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
                Integer.parseInt(splitdate[1]),
                Integer.parseInt(splitdate[0]));
        this.endDateStamp = calendar.getTimeInMillis();
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public Integer getNumOfSeats() {
        return numOfSeats;
    }

    public Long getStartDateStamp() {
        return startDateStamp;
    }

    public Long getEndDateStamp() {
        return endDateStamp;
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

    public Integer getMileage() {
        return mileage;
    }
}
