package com.acrutchfield.natashashairstudio.model;

import java.util.Map;

public class Product {
    private Map<String, Integer> length;
    private String priceRange;
    private String title;
    private String collection;
    private String imageUrl;

    public Product() {
    }

    public Product(Map<String, Integer> length, String priceRange, String title, String collection, String imageUrl) {
        this.length = length;
        this.priceRange = priceRange;
        this.title = title;
        this.collection = collection;
        this.imageUrl = imageUrl;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Integer> getLength() {
        return length;
    }

    public void setLength(Map<String, Integer> length) {
        this.length = length;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
