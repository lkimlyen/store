package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.ProductListEntity;
import com.demo.architect.data.model.offline.CreatePalletModel;
import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.architect.data.model.offline.ExportModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.MessageModel;
import com.demo.architect.data.model.offline.ProductListModel;
import com.demo.architect.data.model.offline.ProductModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.RealmResults;
import rx.Completable;
import rx.Observable;



public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();



    Observable<IPAddress> findIPAddress();

    Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model);


    Observable<String> addProductForPallet(List<ProductListEntity> list, int batch);

    Observable<List<HashMap<ProductListModel, ProductModel>>> checkBarcodeScan(String barcode, long orderId, int batch);

    Observable<String> saveBarcodeResidual(ProductListModel productListModel, ProductModel productModel, final int batch, final String barcode);

    Observable<String> updateNumberScanCreatePallet(long masterId,long id, int number);

    Observable<LinkedHashMap<String, List<DetailProductScanModel>>> getListCreatePalletPrint(long orderId, int batch, int floorId);

    Observable<String> getListCreatePalletUpload(long orderId, int batch, int floorId);

    Observable<RealmResults<CreatePalletModel>> getListScanPallet(long orderId, int batch);

    Observable<String> updateStatusScanPallet(long orderId, int batch, int floorId);

    Observable<RealmResults<ExportModel>> getListScanExport(long orderId, int batch, int floor);

    Observable<String> saveExportPallet(long orderId, int batch, int floor, String code);

    Observable<String> deleteScanPallet(long id, long detaiId);

    Observable<HashMap<Boolean, Integer>> checkEnoughPackPrint(long orderId, int floorId, int batch);

    Observable<String> deleteDataLocal();
}
