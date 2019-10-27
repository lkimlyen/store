package com.demo.store.manager;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductListEntity;

import java.util.ArrayList;
import java.util.List;

public class ListProductManager {
    private List<ProductListEntity> listProduct = new ArrayList<>();
    private static ListProductManager instance;

    public static ListProductManager getInstance() {
        if (instance == null) {
            instance = new ListProductManager();
        }
        return instance;
    }

    public List<ProductListEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductListEntity> listProduct) {
        this.listProduct = listProduct;
    }

//    public  ProductListEntity getProductById(long productId) {
//        for (ProductListEntity requestEntity : listProduct) {
//            if (requestEntity.get() == productId) {
//                return requestEntity;
//            }
//        }
//        return null;
//    }

    public  ProductEntity getProductByBarcode(String barcode) {
        for (ProductListEntity requestEntity : listProduct) {

            for (ProductEntity item : requestEntity.getProductList()){
                if (item.getProductCode().equals(barcode)) {
                    return item;
                }
            }

        }
        return null;
    }
}
