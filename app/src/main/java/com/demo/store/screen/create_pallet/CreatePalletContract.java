package com.demo.store.screen.create_pallet;

import com.demo.architect.data.model.FloorEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.CreatePalletModel;
import com.demo.architect.data.model.offline.ProductListModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.store.app.base.BasePresenter;
import com.demo.store.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface CreatePalletContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListFloor(List<FloorEntity> list);

        void showListScanPallet(RealmResults<CreatePalletModel> parent);

        void showListSO(List<SOEntity> list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void refreshLayout();

        void initList(boolean init);

        void showDialogWarningResidual(ProductListModel productListModel, ProductModel productModel, String barcode);

        void goToPrint();

    }

    interface Presenter extends BasePresenter {
        void getListFloor(long orderId);

        void getListSO(int orderType);

        void getListProduct(long orderId, int batch);

        void checkBarcode(String barcode, long orderId, int batch);

        void deleteScanPallet(long id, long detailId);

        void uploadData(long orderId);

        void saveBarcodeResidual(ProductListModel productListModel, ProductModel productModel, final int batch, String barcode);

        void updateNumberScan(long masterId, long id, int number);

        void getListScanPallet(long orderId, int batch);

        void checkEnoughPack(long orderId, int floorId, int batch);


    }
}
