package com.example.blog_javacode.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Product {

    @SerializedName("ID")
    @Expose
    private final String id;
    @SerializedName("Account")
    @Expose
    private final String account;
    @SerializedName("Name")
    @Expose
    private final String name;
    @SerializedName("Brand")
    @Expose
    private final String brand;
    @SerializedName("Origin")
    @Expose
    private final String origin;
    @SerializedName("Category")
    @Expose
    private final String category;
    @SerializedName("UsedFor")
    @Expose
    private final String usedFor;
    @SerializedName("Descrip")
    @Expose
    private final String descrip;
    @SerializedName("Uses")
    @Expose
    private final String uses;
    @SerializedName("Times")
    @Expose
    private final String times;
    @SerializedName("NOCmt")
    @Expose
    private final String nOCmt;
    @SerializedName("Images")
    @Expose
    private final String images;

    public Product(String id, String account, String name, String brand,
                   String origin, String category, String usedFor, String descrip,
                   String uses, String times, String nOCmt, String images) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.brand = brand;
        this.origin = origin;
        this.category = category;
        this.usedFor = usedFor;
        this.descrip = descrip;
        this.uses = uses;
        this.times = times;
        this.nOCmt = nOCmt;
        this.images = images;
    }

    public String getId() {
    return id;
    }

    public String getAccount() {
    return account;
    }

    public String getName() {
    return name;
    }

    public String getBrand() {
    return brand;
    }

    public String getOrigin() {
    return origin;
    }

    public String getCategory() {
    return category;
    }

    public String getUsedFor() {
    return usedFor;
    }

    public String getDescrip() {
    return descrip;
    }

    public String getUses() {
    return uses;
    }

    public String getTimes() {
    return times;
    }

    public String getNOCmt() {
    return nOCmt;
    }

    public String getImages() {
    return images;
    }
    public String getFirstImage() {
        return Arrays.asList(images.split("\\|")).get(0);
    }
}