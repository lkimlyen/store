package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListEntity {
    @SerializedName("GroupID")
    @Expose
    private long id;

    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("FloorID")
    @Expose
    private int floorId;
    @SerializedName("FloorName")
    @Expose
    private String floorName;
    @SerializedName("ApartmentName")
    @Expose
    private String apartmentName;

    @SerializedName("ApartmentID")
    @Expose
    private int apartmentId;

    @SerializedName("QuantityInFloor")
    @Expose
    private int number;

    @SerializedName("ListOfDepartmentProduct")
    @Expose
    private List<ProductEntity> productList;

    public long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public int getFloorId() {
        return floorId;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public int getNumber() {
        return number;
    }

    public List<ProductEntity> getProductList() {
        return productList;
    }

    public String getFloorName() {
        return floorName;
    }

    public String getApartmentName() {
        return apartmentName;
    }
}
