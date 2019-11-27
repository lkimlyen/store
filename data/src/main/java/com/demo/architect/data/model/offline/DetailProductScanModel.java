package com.demo.architect.data.model.offline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DetailProductScanModel extends RealmObject {
    @PrimaryKey
    private long id;

    @SerializedName("CodeScan")
    @Expose
    private String barcode;
    @Expose
    @SerializedName("ProductID")
    private long productId;

    private String productName;

    private String productCode;
    private int numberPack;
    private Date date;
    @SerializedName("Quantity")
    @Expose
    private int number;

    @SerializedName("Pack")
    @Expose
    private String pack;
    private int maxNumber;

    @SerializedName("Turn")
    @Expose
    private int batch;

    private LogScanProduct logScanProduct;

    public DetailProductScanModel() {

    }

    public DetailProductScanModel( String barcode, long productId, String productName, String productCode, int numberPack, int number, String pack, int maxNumber, int batch) {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.numberPack = numberPack;
        this.maxNumber = maxNumber;
        this.barcode = barcode;
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.number = number;
        this.pack = pack;
        this.batch = batch;
    }

    public static DetailProductScanModel create(Realm realm, DetailProductScanModel model) {
        model = realm.copyToRealm(model);
        return model;
    }

    public static void updateNumber(Realm realm, long masterId, long id, int number) {
        CreatePalletModel createPalletModel = realm.where(CreatePalletModel.class).equalTo("id", masterId).findFirst();
        DetailProductScanModel model = createPalletModel.getProductList().where().equalTo("id", id).findFirst();
        ProductListModel productListModel = realm.where(ProductListModel.class).equalTo("id", createPalletModel.getGroupId()).findFirst();
        ProductModel productModel = productListModel.getProductList().where().equalTo("productId", model.getProductId()).findFirst();
        int ratio = number - model.getNumber();
        productModel.setScanned(productModel.getScanned() + ratio);
        model.setNumber(number);
        LogScanProduct log = model.getLogScanProduct();
        log.setNumberScanned(log.getNumberScanned() + ratio);
        log.setNumberRest(log.getNumberTotal() - log.getNumberScanned());
    }



    public long getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getNumber() {
        return number;
    }

    public String getPack() {
        return pack;
    }

    public int getBatch() {
        return batch;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLogScanProduct(LogScanProduct logScanProduct) {
        this.logScanProduct = logScanProduct;
    }

    public LogScanProduct getLogScanProduct() {
        return logScanProduct;
    }

    public int getNumberPack() {
        return numberPack;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
