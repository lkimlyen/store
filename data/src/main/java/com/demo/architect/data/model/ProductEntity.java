package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductEntity {
    @SerializedName("ProductID")
    @Expose
    private long productId;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("ProductCode")
    @Expose
    private String productCode;
    @SerializedName("CodeScan")
    @Expose
    private String barcode;
    @SerializedName("pack")
    @Expose
    private String pack;
    @SerializedName("QuantityInApartment")
    @Expose
    private int number;

    @SerializedName("Scanned")
    @Expose
    private int scanned;

    public ProductEntity() {
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getNumber() {
        return number;
    }

    public int getScanned() {
        return scanned;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getPack() {
        return pack;
    }
}
