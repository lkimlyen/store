package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.ProductEntity;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long productId;
    private String name;
    private String code;
    private String barcode;
    private String pack;
    private int number;
    private int scanned;

    public ProductModel() {
    }

    public ProductModel(long productId, String name, String code, String barcode, String pack, int number, int scanned) {
        id = UUID.randomUUID().getMostSignificantBits();
        this.productId = productId;
        this.name = name;
        this.code = code;
        this.barcode = barcode;
        this.pack = pack;
        this.number = number;
        this.scanned = scanned;
    }

    public static ProductModel createOrUpdate(Realm realm, ProductEntity product, LogScanProduct logScanProduct) {
        ProductModel productModel;

        productModel = new ProductModel(product.getProductId(), product.getProductName(),
                product.getProductCode(), product.getBarcode(), product.getPack(), product.getNumber(), product.getScanned());

        if (logScanProduct != null) {
            productModel.setScanned(logScanProduct.getNumberScanned());
        }
        productModel = realm.copyToRealmOrUpdate(productModel);
        return productModel;
    }

    public long getId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
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

    public void setScanned(int scanned) {
        this.scanned = scanned;
    }
}
