package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentEntity {

    @SerializedName("ApartmentID")
    @Expose
    private int id;

    @SerializedName("ApartmentCode")
    @Expose
    private String code;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
