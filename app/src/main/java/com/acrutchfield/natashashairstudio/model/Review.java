package com.acrutchfield.natashashairstudio.model;

public class Review {
    private String date;
    private String details;
    private String rating;
    private String service;
    private String uid;
    private String photoUrl;
    private String clientName;

    public Review() {
    }

    public Review(String date, String details, String rating, String service, String uid, String photoUrl, String clientName) {
        this.date = date;
        this.details = details;
        this.rating = rating;
        this.service = service;
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.clientName = clientName;
    }

    private Review(Builder builder) {
        setDate(builder.date);
        setDetails(builder.details);
        setRating(builder.rating);
        setService(builder.service);
        setUid(builder.uid);
        setPhotoUrl(builder.photoUrl);
        setClientName(builder.clientName);
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public static final class Builder {
        private String date;
        private String details;
        private String rating;
        private String service;
        private String uid;
        private String photoUrl;
        private String clientName;

        public Builder() {
        }

        public Builder date(String val) {
            date = val;
            return this;
        }

        public Builder details(String val) {
            details = val;
            return this;
        }

        public Builder rating(String val) {
            rating = val;
            return this;
        }

        public Builder service(String val) {
            service = val;
            return this;
        }

        public Builder uid(String val) {
            uid = val;
            return this;
        }

        public Builder photoUrl(String val) {
            photoUrl = val;
            return this;
        }

        public Builder clientName(String val) {
            clientName = val;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }
}
