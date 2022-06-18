package com.example.blog_javacode.article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Article {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("Account")
    @Expose
    private String account;
    @SerializedName("Headers")
    @Expose
    private String headers;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Body")
    @Expose
    private String body;
    @SerializedName("Images")
    @Expose
    private String images;
    @SerializedName("Times")
    @Expose
    private String times;

    public Article(String id, String account, String headers, String category, String body, String images, String times) {
        this.id = id;
        this.account = account;
        this.headers = headers;
        this.category = category;
        this.body = body;
        this.images = images;
        this.times = times;
    }

    public String getId() {
    return id;
    }

    public String getAccount() {
    return account;
    }

    public String getHeaders() {
    return headers;
    }

    public String getCategory() {
    return category;
    }

    public String getBody() {
    return body;
    }

    public String getImages() {
        return images;
    }
    public String getFirstImage() {
        return Arrays.asList(images.split("\\|")).get(0);
    }

    public String getTimes() {
    return times;
    }


}