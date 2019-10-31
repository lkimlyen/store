package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ProductListEntity;
import com.demo.architect.data.model.offline.CreatePalletModel;
import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.architect.data.model.offline.ExportModel;
import com.demo.architect.data.model.offline.ProductListModel;
import com.demo.architect.data.model.offline.ProductModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmSchema;

public class DatabaseRealm {
    private Context context;
    private long userId = -1;

    public DatabaseRealm() {
    }

    public DatabaseRealm(Context context) {
        this.context = context;
    }

    public Realm getRealmInstance() {
        if (!RealmHelper.getInstance().getInitRealm()) {
            if (SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "").equals(Constants.SERVER_MAIN)) {
                RealmConfiguration realmConfigurationMain = new RealmConfiguration.Builder()
                        .name(Constants.DATABASE_MAIN)
                        .schemaVersion(1)
                        .migration(new MyMigration())
                        .build();
                Realm.setDefaultConfiguration(realmConfigurationMain);
                RealmHelper.getInstance().initRealm(true);
            }
            if (SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "").equals(Constants.SERVER_TEST)) {
                RealmConfiguration realmConfigurationTest = new RealmConfiguration.Builder()
                        .name(Constants.DATABASE_TEST)
                        .schemaVersion(1)
                        .migration(new MyMigration())
                        .build();
                Realm.setDefaultConfiguration(realmConfigurationTest);
                RealmHelper.getInstance().initRealm(true);

            }

            userId = SharedPreferenceHelper.getInstance(context).getUserObject().getId();

        }
        return Realm.getDefaultInstance();
    }

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> void addItemAsync(final T item) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    public <T extends RealmObject> void insertOrUpdate(final T item) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    public <T extends RealmObject> T findFirst(Class<T> clazz) {
        return getRealmInstance().where(clazz).findFirst();
    }

    public <T extends RealmObject> T findFirstById(Class<T> clazz, long id) {
        return getRealmInstance().where(clazz).equalTo("id", id).findFirst();
    }

    public void close() {
        getRealmInstance().close();
    }

    public <T extends RealmObject> void delete(Class<T> clazz) {
        Realm realm = getRealmInstance();
        // obtain the results of a query
        final RealmResults<T> results = realm.where(clazz).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();

            }
        });
    }


    public void addProductForPallet(final List<ProductListEntity> list, final int batch) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductListModel.createOrUpdate(realm, list, batch);
            }
        });

    }

    public List<HashMap<ProductListModel, ProductModel>> checkBarcodeScan(String barcode, long orderId, int batch) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        List<HashMap<ProductListModel, ProductModel>> result = ProductListModel.checkBarcodeScan(realm, barcode, orderId, batch);
        realm.commitTransaction();
        return result;
    }

    public void saveBarcodeResidual(final ProductListModel productListModel, final ProductModel productModel, final int batch, final String barcode) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CreatePalletModel.create(realm, productModel, productListModel, barcode,batch);
            }
        });
    }

    public void updateNumberScanCreatePallet(final long masterId, final long id, final int number) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DetailProductScanModel.updateNumber(realm, masterId, id, number);
            }
        });
    }

    public LinkedHashMap<String, List<DetailProductScanModel>> getListCreatePalletPrint(long orderId, int batch, int floorId) {
        Realm realm = getRealmInstance();
        LinkedHashMap<String, List<DetailProductScanModel>> result = CreatePalletModel.getListCreatePalletPrint(realm, orderId, batch, floorId);
        return result;
    }

    public String getListCreatePalletUpload(long orderId, int batch, int floorId) {
        Realm realm = getRealmInstance();
        String json = CreatePalletModel.getListCreatePalletUpload(realm,orderId, batch, floorId);
        return json;
    }

    public RealmResults<CreatePalletModel> getListScanPallet(long orderId, int batch) {
        Realm realm = getRealmInstance();
        RealmResults<CreatePalletModel> results = CreatePalletModel.getListScanPallet(realm,orderId, batch);
        return results;
    }

    public void updateStatusScanPallet(final long orderId, final int batch, final int floorId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CreatePalletModel.updateStatusScanPallet(realm,orderId,batch,floorId);
            }
        });
    }

    public RealmResults<ExportModel> getListScanExport(long orderId, int batch, int floor) {
        Realm realm = getRealmInstance();
        RealmResults<ExportModel> results = ExportModel.getListExportByDate(realm,orderId,batch,floor);
        return results;
    }

    public void saveExportPallet(final long orderId, final int batch, final int floor, final String code) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ExportModel.create(realm,orderId,batch,floor,code);
            }
        });
    }

    public void deleteScanPallet(final long id, final long detaiId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CreatePalletModel.deleteScanPallet(realm,id,detaiId);
            }
        });
    }

    public HashMap<Boolean, Integer> checkEnoughPackPrint(long orderId, int floorId, int batch) {
        Realm realm = getRealmInstance();
        HashMap<Boolean, Integer> enough = CreatePalletModel.checkEnoughPackPrint(realm,orderId,floorId,batch);
        return enough;
    }

    public void deleteDataLocal() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                 realm.deleteAll();
            }
        });
    }

    public class MyMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();


        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof MyMigration; // obj instance of your Migration class name, here My class is Migration.
        }

        @Override
        public int hashCode() {
            return MyMigration.class.hashCode();
        }
    }

}
