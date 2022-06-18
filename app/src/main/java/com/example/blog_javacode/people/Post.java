package com.example.blog_javacode.people;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Post {

    @SerializedName("Account")
    @Expose
    private final String account;
    @SerializedName("Name")
    @Expose
    private final String name;
    @SerializedName("Avatar")
    @Expose
    private final String avatar;
    @SerializedName("ID")
    @Expose
    private final String id;
    @SerializedName("Text")
    @Expose
    private final String text;
    @SerializedName("Noview")
    @Expose
    private final Integer noview;
    @SerializedName("Nocmt")
    @Expose
    private final Integer nocmt;
    @SerializedName("Times")
    @Expose
    private final String times;
    @SerializedName("Links")
    @Expose
    private final String links;

    private final Boolean checkaccount;

    public Post(String account, String name, String avatar, String id, String text, Integer noview, Integer nocmt, String times, String links, Boolean checkaccount) {
        this.account = account;
        this.name = name;
        this.avatar = avatar;
        this.id = id;
        this.text = text;
        this.noview = noview;
        this.nocmt = nocmt;
        this.times = times;
        this.links = links;
        this.checkaccount = checkaccount;
    }

    public Boolean getCheckaccount() {
        return checkaccount;
    }

    public String getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getNoview() {
        return noview;
    }

    public Integer getNocmt() {
        return nocmt;
    }

    public String getTimes() {
        return times;
    }

    public String getLinks() {
        return links;
    }
    public String getFirstImage() {
        if(links != null)
            return Arrays.asList(links.split("\\|")).get(0);
        return null;
    }
}