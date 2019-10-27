package com.demo.architect.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FloorEntity {
    @SerializedName("FloorID")
    @Expose
    private int floorId;

    @SerializedName("FloorName")
    @Expose
    private String floorName;

    @SerializedName("ListApartment")
    @Expose
    private List<ApartmentEntity> apartmentList;

    public int getFloorId() {
        return floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public List<ApartmentEntity> getApartmentList() {
        return apartmentList;
    }

    @NonNull
    @Override
    public String toString() {
        return floorName;
    }
}
