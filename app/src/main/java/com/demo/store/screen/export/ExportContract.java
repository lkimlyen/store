package com.demo.store.screen.export;

import com.demo.architect.data.model.FloorEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.CreatePalletModel;
import com.demo.architect.data.model.offline.ExportModel;
import com.demo.architect.data.model.offline.ProductListModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.store.app.base.BasePresenter;
import com.demo.store.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ExportContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListFloor(List<FloorEntity> list);

        void showListExportPallet(RealmResults<ExportModel> parent);

        void showListSO(List<SOEntity> list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

    }

    interface Presenter extends BasePresenter {
        void getListFloor(long orderId);

        void getListSO(int orderType);

        void getListCodePallet(long orderId, int batch, int floor);

        void checkBarcode(String barcode, long orderId, int batch, int floorId);


        void getListScanExport(long orderId, int batch, int floor);

    }
}
