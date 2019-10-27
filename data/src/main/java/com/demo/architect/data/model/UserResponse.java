package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse extends BaseResponse {
    @SerializedName("UserInfo")
    @Expose
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }
}
