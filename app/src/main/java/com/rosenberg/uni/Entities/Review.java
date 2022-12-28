package com.rosenberg.uni.Entities;

public class Review {
    float rating;
    String comment;

    public Review() {
    }

    public Review(String comment, float rating) {
        this.rating = rating;
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
