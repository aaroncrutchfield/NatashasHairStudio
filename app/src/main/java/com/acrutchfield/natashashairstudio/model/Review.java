package com.acrutchfield.natashashairstudio.model;

public class Review {
    private String date;
    private String details;
    private int rating;
    private String service;
    private String uid;

    public Review() {
    }

    public Review(String date, String details, int rating, String service, String uid) {
        this.date = date;
        this.details = details;
        this.rating = rating;
        this.service = service;
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
