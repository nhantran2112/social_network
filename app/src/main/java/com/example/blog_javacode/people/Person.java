package com.example.blog_javacode.people;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("Account")
    @Expose
    private final String account;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Name")
    @Expose
    private final String name;
    @SerializedName("Intro")
    @Expose
    private final String intro;
    @SerializedName("View")
    @Expose
    private final Integer view;
    @SerializedName("ProductNumber")
    @Expose
    private final Integer productNumber;
    @SerializedName("PostNumber")
    @Expose
    private final Integer postNumber;
    @SerializedName("Avatar")
    @Expose
    private final String avatar;


    public Person(String account, String name, String intro, Integer view, Integer productNumber, Integer postNumber, String avatar) {
        this.account = account;
        this.name = name;
        this.intro = intro;
        this.view = view;
        this.productNumber = productNumber;
        this.postNumber = postNumber;
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public String getIntro() {
        return intro;
    }

    public String getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public Integer getView() {
        return view;
    }

    public Integer getProductNumber() {
        return productNumber;
    }

    public Integer getPostNumber() {
        return postNumber;
    }

    public String getAvatar() {
        return avatar;
    }
}