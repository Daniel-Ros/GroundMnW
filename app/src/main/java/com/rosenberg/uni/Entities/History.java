package com.rosenberg.uni.Entities;

public class History {
    String renterID;
    Boolean reviewed;
    String name;

    public History(String renterID,String name, Boolean reviewed) {
        this.renterID = renterID;
        this.reviewed = reviewed;
        this.name = name;
    }

    public History() {
    }

    public String getRenterID() {
        return renterID;
    }

    public void setRenterID(String renterID) {
        this.renterID = renterID;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }

    public String getName() {
        return name;
    }
}
