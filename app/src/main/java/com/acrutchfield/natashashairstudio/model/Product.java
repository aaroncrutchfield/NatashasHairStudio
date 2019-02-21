package com.acrutchfield.natashashairstudio.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Product implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.length.size());
        for (Map.Entry<String, Integer> entry : this.length.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
        dest.writeString(this.priceRange);
        dest.writeString(this.title);
        dest.writeString(this.collection);
        dest.writeString(this.imageUrl);
    }

    protected Product(Parcel in) {
        int lengthSize = in.readInt();
        this.length = new HashMap<String, Integer>(lengthSize);
        for (int i = 0; i < lengthSize; i++) {
            String key = in.readString();
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.length.put(key, value);
        }
        this.priceRange = in.readString();
        this.title = in.readString();
        this.collection = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
