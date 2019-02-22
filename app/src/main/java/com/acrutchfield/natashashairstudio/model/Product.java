package com.acrutchfield.natashashairstudio.model;

import java.util.Map;

public class Product {
    Map<String, Integer> length;
    String priceRange;
    String title;

    public Product() {
    }

    public Product(Map<String, Integer> length, String priceRange, String title) {
        this.length = length;
        this.priceRange = priceRange;
        this.title = title;
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
