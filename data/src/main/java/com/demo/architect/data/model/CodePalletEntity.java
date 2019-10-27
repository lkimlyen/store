package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CodePalletEntity {
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("CountNum")
    @Expose
    private String countNum;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("PalletStatus")
    @Expose
    private int status;

    @SerializedName("PalletStatusDecription")
    @Expose
    private String description;

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCountNum() {
        return countNum;
    }
}
