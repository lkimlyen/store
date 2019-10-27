package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEntity {
    @SerializedName("UserID")
    @Expose
    private long id;

    @SerializedName("FullName")
    @Expose
    private String name;

    @SerializedName("UserRoleName")
    @Expose
    private String userRoleName;
    @SerializedName("UserRoleID")
    @Expose
    private int role;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public int getRole() {
        return role;
    }
}
