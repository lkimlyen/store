package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductListEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class ProductListModel extends RealmObject {
    @PrimaryKey
    private long id;

    private long orderId;

    private int floorId;
    private String floorName;

    private int apartmentId;
    private String apartmentName;
    private int batch;

    private int number;

    private RealmList<ProductModel> productList;

    public ProductListModel() {

    }

    public ProductListModel(long id, long orderId, int floorId, String floorName, int apartmentId, String apartmentName, int batch, int number) {
        this.id = id;
        this.orderId = orderId;
        this.floorId = floorId;
        this.apartmentId = apartmentId;
        this.floorName = floorName;
        this.apartmentName = apartmentName;
        this.batch = batch;
        this.number = number;
    }

    public static void createOrUpdate(Realm realm, List<ProductListEntity> productList, int batch) {
        for (ProductListEntity item : productList) {
            ProductListModel productListModel = new ProductListModel(item.getId(), item.getOrderId(), item.getFloorId(), item.getFloorName(),
                    item.getApartmentId(), item.getApartmentName(), batch, item.getNumber());
            productListModel = realm.copyToRealmOrUpdate(productListModel);
            RealmList<ProductModel> list = productListModel.getProductList();
            for (ProductEntity entity : item.getProductList()) {
                //  int numberTotal = item.getNumber() * entity.getNumber();
                LogScanProduct logScanProduct = realm.where(LogScanProduct.class).equalTo("groupId", item.getId())
                        .equalTo("productId", entity.getProductId())
                        .equalTo("status", Constants.DOING).findFirst();
                if (logScanProduct != null) {
                    if (entity.getScanned() > logScanProduct.getNumberScanned()) {
                        logScanProduct.setNumberScanned(entity.getScanned());
                    }
                    //logScanProduct.setNumberTotal(numberTotal);
                    logScanProduct.setNumberRest(logScanProduct.getNumberRest());
                }

                ProductModel productModel = ProductModel.createOrUpdate(realm, entity, logScanProduct);
                list.add(productModel);
            }
        }
    }

    public static List<HashMap<ProductListModel, ProductModel>> checkBarcodeScan(Realm realm, String barcode, long orderId, int batch) {
        List<HashMap<ProductListModel, ProductModel>> result = new ArrayList<>();
        RealmResults<ProductListModel> productListModel = realm.where(ProductListModel.class).equalTo("orderId", orderId)
                .equalTo("batch", batch).findAll().sort("apartmentId", Sort.ASCENDING).sort("floorId", Sort.ASCENDING);

        String packCode = barcode.substring(barcode.length() - 5);
        int numberPack = Integer.parseInt(packCode.substring(packCode.lastIndexOf("/") + 1));
        for (ProductListModel model : productListModel) {
            for (ProductModel product : model.getProductList()) {
                String code = product.getCode().replace("-", "");
                if (barcode.contains(code)) {
                    int numberTotal = product.getNumber() * model.getNumber() * numberPack;
                    if (product.getScanned() < numberTotal) {
                        LogScanProduct log = realm.where(LogScanProduct.class).equalTo("groupId", model.getId()).equalTo("productId", product.getId()).findFirst();
                        CreatePalletModel createPalletModel = realm.where(CreatePalletModel.class)
                                .equalTo("groupId", model.getId())
                                .equalTo("status", Constants.WAITING_UPLOAD)
                                .findFirst();
                        if (log != null) {
                            if (log.getNumberRest() > 0 && createPalletModel != null) {
//                            if () {
//                                if (log.getNumberRest() == 1) {

                                RealmList<DetailProductScanModel> detailList = createPalletModel.getProductList();
//                                        boolean checkExist = false;
//                                        for (int i = 1; i <= numberPack; i++) {
//                                            for (DetailProductScanModel detail : detailList) {
//                                                String pack = "0" + i + "/" + "0" + numberPack;
//                                                if (detail.getPack().equals(pack)) {
//                                                    checkExist = true;
//                                                    break;
//                                                } else {
//                                                    checkExist = false;
//                                                }
//                                            }
//                                        }
//                                        if (checkExist) {
//                                            CreatePalletModel.create(realm, product, model, barcode, batch);
//                                            return null;
//                                        } else {
                                DetailProductScanModel detail = detailList.where().equalTo("pack", packCode)
                                        .equalTo("productId", product.getId()).findFirst();
                                if (detail == null || (detail.getNumber() < detail.getMaxNumber())) {
                                    CreatePalletModel.create(realm, product, model, barcode, batch);
                                    return null;
                                } else {
                                    HashMap<ProductListModel, ProductModel> hashMap = new HashMap<>();
                                    hashMap.put(model, product);
                                    if (result.size() > 0) {
                                        result.set(0, hashMap);
                                    } else {

                                        result.add(hashMap);
                                    }
                                }
                            } else {
                                HashMap<ProductListModel, ProductModel> hashMap = new HashMap<>();
                                hashMap.put(model, product);
                                if (result.size() > 0) {
                                    result.set(0, hashMap);
                                } else {

                                    result.add(hashMap);
                                }
                            }

                        } else {
                            CreatePalletModel.create(realm, product, model, barcode, batch);
                            return null;
                        }
//                                        }
//
//                                } else {
//                                    CreatePalletModel.create(realm, product, model, barcode, batch);
//                                    return null;
//                                }
                    } else {
                        HashMap<ProductListModel, ProductModel> hashMap = new HashMap<>();
                        hashMap.put(model, product);
                        if (result.size() > 0) {
                            result.set(0, hashMap);
                        } else {
                            result.add(hashMap);
                        }
                    }

                }
            }
        }

        return result;
    }


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

    public String getFloorName() {
        return floorName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public RealmList<ProductModel> getProductList() {
        return productList;
    }
}
