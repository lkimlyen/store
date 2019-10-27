package com.demo.architect.data.repository.base.local;

import android.content.Context;

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
import rx.Observable;
import rx.Subscriber;

public class LocalRepositoryImpl implements LocalRepository {

    DatabaseRealm databaseRealm;

    public LocalRepositoryImpl(Context context) {
        databaseRealm = new DatabaseRealm(context);
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.add(model);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<MessageModel>>() {
            @Override
            public void call(Subscriber<? super List<MessageModel>> subscriber) {
                try {
                    List<MessageModel> models = databaseRealm.findAll(MessageModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }




    @Override
    public Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model) {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    databaseRealm.insertOrUpdate(model);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<IPAddress> findIPAddress() {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    IPAddress ipAddress = databaseRealm.findFirst(IPAddress.class);
                    subscriber.onNext(ipAddress);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> addProductForPallet(final List<ProductListEntity> list, final int batch) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addProductForPallet (list, batch);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<HashMap<ProductListModel, ProductModel>>> checkBarcodeScan(final String barcode, final long orderId, final int batch) {
        return Observable.create(new Observable.OnSubscribe<List<HashMap<ProductListModel, ProductModel>>>() {
            @Override
            public void call(Subscriber<? super List<HashMap<ProductListModel, ProductModel>>> subscriber) {
                try {
                    List<HashMap<ProductListModel, ProductModel>> result =  databaseRealm.checkBarcodeScan (barcode,orderId,batch);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveBarcodeResidual(final ProductListModel productListModel, final ProductModel productModel, final int batch, final String barcode) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveBarcodeResidual (productListModel, productModel,batch,barcode);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberScanCreatePallet(final long masterId,final long id, final int number) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberScanCreatePallet (masterId,id, number);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LinkedHashMap<String, List<DetailProductScanModel>>> getListCreatePalletPrint(final long orderId, final int batch, final int floorId) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<String, List<DetailProductScanModel>>>() {
            @Override
            public void call(Subscriber<? super LinkedHashMap<String, List<DetailProductScanModel>>> subscriber) {
                try {
                    LinkedHashMap<String, List<DetailProductScanModel>> result = databaseRealm.getListCreatePalletPrint (orderId,batch,floorId);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> getListCreatePalletUpload(final long orderId, final int batch, final int floorId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                   String json =  databaseRealm.getListCreatePalletUpload (orderId,batch,floorId);
                    subscriber.onNext(json);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<CreatePalletModel>> getListScanPallet(final long orderId, final int batch) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<CreatePalletModel>>() {
            @Override
            public void call(Subscriber<? super RealmResults<CreatePalletModel>> subscriber) {
                try {
                    RealmResults<CreatePalletModel> results =  databaseRealm.getListScanPallet (orderId,batch);
                    subscriber.onNext(results);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanPallet(final long orderId, final int batch, final int floorId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                     databaseRealm.updateStatusScanPallet (orderId,batch,floorId);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<ExportModel>> getListScanExport(final long orderId, final int batch, final int floor) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<ExportModel>>() {
            @Override
            public void call(Subscriber<? super RealmResults<ExportModel>> subscriber) {
                try {
                    RealmResults<ExportModel> results =  databaseRealm.getListScanExport (orderId,batch,floor);
                    subscriber.onNext(results);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveExportPallet(final long orderId, final int batch, final int floor, final String code) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveExportPallet (orderId,batch,floor,code);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteScanPallet(final long id, final long detaiId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteScanPallet (id, detaiId);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkEnoughPackPrint(final long orderId, final int floorId, final int batch) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                   boolean enough = databaseRealm.checkEnoughPackPrint (orderId,floorId,batch);
                    subscriber.onNext(enough);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteDataLocal() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteDataLocal ();
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
