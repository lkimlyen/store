package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class CreatePalletModel extends RealmObject {
    @PrimaryKey
    private long id;

    @Expose
    @SerializedName("PalletID")
    private long groupId;

    private long orderId;
    private int batch;

    @Expose
    @SerializedName("ProductID")
    private long productId;


    @Expose
    @SerializedName("Quantity")
    private int number;

    private int floorId;
    private String floorName;

    private int apartmentId;

    private String apartmentName;
    private int status;
    @SerializedName("ListCodeDetailProductScan")
    @Expose
    private RealmList<DetailProductScanModel> productList;

    public CreatePalletModel() {
    }

    public CreatePalletModel(long groupId, long orderId, long productId, int number, int floorId, String floorName, int apartmentId, String apartmentName, int batch) {
        this.orderId = orderId;
        id = UUID.randomUUID().getMostSignificantBits();
        this.groupId = groupId;
        this.productId = productId;
        this.number = number;
        this.floorId = floorId;
        this.floorName = floorName;
        this.apartmentId = apartmentId;
        this.apartmentName = apartmentName;
        this.batch = batch;
        status = Constants.WAITING_UPLOAD;
    }

    public static void create(Realm realm, ProductModel productModel, ProductListModel productListModel, String barcode, int batch) {
        String packCode = barcode.substring(barcode.length()-5);
        CreatePalletModel createPalletModel = realm.where(CreatePalletModel.class)
                .equalTo("groupId", productListModel.getId())
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findFirst();

        if (createPalletModel == null) {
            createPalletModel = new CreatePalletModel(productListModel.getId(), productListModel.getOrderId(), productModel.getId(),
                    productListModel.getNumber(), productListModel.getFloorId(),
                    productListModel.getFloorName(), productListModel.getApartmentId(), productListModel.getApartmentName(), batch);
            createPalletModel = realm.copyToRealm(createPalletModel);
        }
        int numberPack = Integer.parseInt(packCode.substring(packCode.lastIndexOf("/") + 1));
        int numberTotal = productListModel.getNumber() * productModel.getNumber()*numberPack;
        RealmList<DetailProductScanModel> detailProductList = createPalletModel.getProductList();
        DetailProductScanModel detailProductScanModel = detailProductList.where().equalTo("productId", productModel.getId()).equalTo("barcode", barcode).findFirst();
        if (detailProductScanModel == null) {
            detailProductScanModel = new DetailProductScanModel(barcode, productModel.getId(), productModel.getName(),
                    productModel.getCode(), numberPack, 1, packCode, productListModel.getNumber() * productModel.getNumber(), batch);
            detailProductScanModel = DetailProductScanModel.create(realm, detailProductScanModel);
            detailProductList.add(detailProductScanModel);
        } else {
            detailProductScanModel.setNumber(detailProductScanModel.getNumber() + 1);
        }
        productModel.setScanned(productModel.getScanned() + 1);

        LogScanProduct logScanProduct = detailProductScanModel.getLogScanProduct();
        if (logScanProduct == null) {
            logScanProduct = realm.where(LogScanProduct.class).equalTo("groupId", productListModel.getId())
                    .equalTo("productId", productModel.getId())
                    .equalTo("status", Constants.DOING).findFirst();
            if (logScanProduct == null) {
                logScanProduct = new LogScanProduct(productListModel.getId(), productModel.getId(),
                        numberTotal, productModel.getScanned(), numberTotal - productModel.getScanned());
                logScanProduct = LogScanProduct.createLog(realm, logScanProduct);

            }else {
                logScanProduct.setNumberTotal(numberTotal);
                logScanProduct.setNumberScanned(logScanProduct.getNumberScanned() + 1);
                logScanProduct.setNumberRest(logScanProduct.getNumberTotal() - logScanProduct.getNumberScanned());
            }
            detailProductScanModel.setLogScanProduct(logScanProduct);

        } else {
            logScanProduct.setNumberScanned(logScanProduct.getNumberScanned() + 1);
            logScanProduct.setNumberRest(logScanProduct.getNumberRest() - 1);
        }
    }


    public static LinkedHashMap<String, List<DetailProductScanModel>> getListCreatePalletPrint(Realm realm, long orderId, int batch, int floorId) {
        RealmResults<CreatePalletModel> realmResults = realm.where(CreatePalletModel.class).equalTo("orderId", orderId)
                .equalTo("batch", batch).equalTo("floorId", floorId).equalTo("status", Constants.WAITING_UPLOAD).distinct("apartmentId").findAll();
        LinkedHashMap<String, List<DetailProductScanModel>> result = new LinkedHashMap<>();
        for (CreatePalletModel model : realmResults) {
            RealmResults<CreatePalletModel> list = realm.where(CreatePalletModel.class).equalTo("orderId", orderId)
                    .equalTo("batch", batch).equalTo("floorId", floorId)
                    .equalTo("status", Constants.WAITING_UPLOAD)
                    .equalTo("apartmentId", model.getApartmentId()).findAll();
            List<DetailProductScanModel> detailList = new ArrayList<>();
            for (CreatePalletModel createModel : list) {
                detailList.addAll(realm.copyFromRealm(createModel.getProductList()));
            }
            result.put(model.getApartmentName(), detailList);
        }
        return result;
    }

    public static String getListCreatePalletUpload(Realm realm, long orderId, int batch, int floorId) {
        RealmResults<CreatePalletModel> realmResults = realm.where(CreatePalletModel.class).equalTo("orderId", orderId)
                .equalTo("batch", batch).equalTo("floorId", floorId).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        List<CreatePalletModel> list = realm.copyFromRealm(realmResults);
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(list);
        return json;
    }

    public static RealmResults<CreatePalletModel> getListScanPallet(Realm realm, long orderId, int batch) {
        RealmResults<CreatePalletModel> realmResults = realm.where(CreatePalletModel.class).equalTo("orderId", orderId)
                .equalTo("batch", batch).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return realmResults;
    }

    public static void updateStatusScanPallet(Realm realm, long orderId, int batch, int floorId) {
        RealmResults<CreatePalletModel> realmResults = realm.where(CreatePalletModel.class).equalTo("orderId", orderId)
                .equalTo("batch", batch).equalTo("floorId", floorId).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        for (CreatePalletModel model : realmResults){
            for (DetailProductScanModel detailProductScanModel : model.getProductList()){
                LogScanProduct log  = detailProductScanModel.getLogScanProduct();
                log.setStatus(Constants.COMPLETE);
            }
            model.setStatus(Constants.COMPLETE);
        }
    }

    public static void deleteScanPallet(Realm realm, long id, long detaiId) {
        CreatePalletModel createPalletModel = realm.where(CreatePalletModel.class).equalTo("id", id).findFirst();
        RealmList<DetailProductScanModel> detailList = createPalletModel.getProductList();
        DetailProductScanModel detail = detailList.where().equalTo("id",detaiId).findFirst();
        LogScanProduct log = detail.getLogScanProduct();
        log.setNumberScanned(log.getNumberScanned() - detail.getNumber());
        log.setNumberRest(log.getNumberTotal() - log.getNumberScanned());
        ProductListModel productListModel = realm.where(ProductListModel.class).equalTo("id",createPalletModel.getGroupId()).findFirst();
        ProductModel productModel = productListModel.getProductList().where().equalTo("productId", detail.getProductId()).findFirst();
        productModel.setScanned(productModel.getScanned() - detail.getNumber() );
        detail.deleteFromRealm();
        if (detailList.size() == 0){
            createPalletModel.deleteFromRealm();
        }
    }

    public static boolean checkEnoughPackPrint(Realm realm,long orderId, int floorId, int batch) {
        RealmResults<CreatePalletModel> realmResults = realm.where(CreatePalletModel.class).equalTo("orderId", orderId)
                .equalTo("batch", batch).equalTo("floorId", floorId).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        if (realmResults.size() == 0){
            return false;
        }
        for (CreatePalletModel model :realmResults){
            for (DetailProductScanModel detail : model.getProductList()){
                LogScanProduct log = detail.getLogScanProduct();
                if (log.getNumberRest() > 0){
                    return false;
                }
            }
        }

        return true;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getProductId() {
        return productId;
    }

    public int getNumber() {
        return number;
    }

    public int getFloorId() {
        return floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public RealmList<DetailProductScanModel> getProductList() {
        return productList;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
