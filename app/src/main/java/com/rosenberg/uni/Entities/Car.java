package com.rosenberg.uni.Entities;

import com.google.firebase.firestore.DocumentId;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Car {
    @DocumentId
    String documentId; // UNIQUE, helpful when we want to get car from db
    // and also in place of updating the car via searching it again at db
    // we can just update it via docID

    private String make;
    private String model;
    private Integer mileage;
    private String ownerID;  // UNIQUE, userId
    private Integer numOfSeats;
    private String fuel;
    private String gearbox;
    private String startDate;
    private Long startDateStamp; // number milli Sec, helps for queries, cant query on the string date
    private String endDate;
    private Long endDateStamp; // same as above
    private Integer price;
    private String picid;

    private String renterID; // UNIQUE, userId - but for the renter of the car
    private List<History> previousRentersID;

    /**
     * default constructor required for calls to DataSnapshot.getValue(Car.class)
     */
    public Car(){

    }

    /**
     * constructor - this obj hold all the data of a car
     * @param make - type, e.g toyota
     * @param model - e.g corolla 2021
     * @param mileage - a.k.a "kilometraz", e.g 25000
     * @param numOfSeats - include driver
     * @param fuel - type, e.g 95
     * @param gearbox - type, e.g auto
     * @param startDate - from time the renter can rent the car
     * @param endDate - to time the renter have to give back the car
     * @param price - for the whole renting time
     * @param ownerID - ownerID equal to User.id -> the car owned by whom
     */
    public Car(String make, String model, String mileage, String numOfSeats, String fuel,
               String gearbox, String startDate, String endDate,
               Integer price, String ownerID) {

        this.make = make;
        this.model = model;
        this.mileage = Integer.parseInt(mileage);
        this.numOfSeats = Integer.parseInt(numOfSeats);
        this.fuel = fuel;
        this.gearbox = gearbox;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownerID = ownerID;
        this.price = price;

        // extract the starting time from string
        // new Calender(year, month, day)
        this.startDateStamp = extractMilisTime(startDate);

        // extract the end time from string
        // new Calender(year, month, day)
        this.endDateStamp = extractMilisTime(endDate);
    }

    /**
     * extract the time from date string
     * to new Calender(year, month, day)
     * @param date string
     * @return time in milis
     */
    private Long extractMilisTime(String date){
        String [] splitdate = endDate.split("/");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
                Integer.parseInt(splitdate[1]),
                Integer.parseInt(splitdate[0]));
        return calendar.getTimeInMillis();
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setRenterID(String renterID) {
        this.renterID = renterID;
    }

    public String getRenterID() {
        return renterID;
    }

    public List<History> getPreviousRentersID() {
        return previousRentersID;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }
}