package com.demo.store.screen.print_pallet;

import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.store.app.base.BasePresenter;
import com.demo.store.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface PrintPalletContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListCreatePalletPrint(LinkedHashMap<String, List<DetailProductScanModel>> list);

        void showDialogCreateIPAddress(String code);

        void printSuccessfully();

    }

    interface Presenter extends BasePresenter {
        void getListCreatePalletPrint(long orderId, int batch, int floorId);

        void getListUpload(long orderId, int batch, int floorId);

        void print(String code,long orderId, int batch, int floorId);

        void saveIPAddress(String ipAddress, int port, long orderId, int batch, int floorId);
    }
}
