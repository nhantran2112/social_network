package com.example.blog_javacode.store;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkShop {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("LinkShop")
    @Expose
    private String linkShop;

    public LinkShop(String name, String linkShop) {
        this.name = name;
        this.linkShop = linkShop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkShop() {
        return linkShop;
    }

}