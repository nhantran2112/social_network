package com.example.blog_javacode.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("ID")
    @Expose
    private final String id;
    @SerializedName("Account")
    @Expose
    private final String account;
    @SerializedName("Name")
    @Expose
    private final String name;
    @SerializedName("Avatar")
    @Expose
    private final String avatar;
    @SerializedName("Comment")
    @Expose
    private final String comment;
    @SerializedName("Times")
    @Expose
    private final String times;

    public Comment(String id, String account, String name, String avatar, String comment, String times) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.avatar = avatar;
        this.comment = comment;
        this.times = times;
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

    public String getAvatar() {
        return avatar;
    }

    public String getComment() {
        return comment;
    }

    public String getTimes() {
        return times;
    }
}