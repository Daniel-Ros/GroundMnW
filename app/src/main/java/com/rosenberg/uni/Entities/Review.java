package com.rosenberg.uni.Entities;

/**
 * presents the review content between user to another
 */
public class Review {
    float rating; // stars 1~5
    String comment; // words details

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
