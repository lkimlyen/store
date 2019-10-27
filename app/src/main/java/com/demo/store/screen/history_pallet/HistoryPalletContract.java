package com.demo.store.screen.history_pallet;

import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.model.FloorEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.store.app.base.BasePresenter;
import com.demo.store.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface HistoryPalletContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListSO(List<SOEntity> list);

        void showListFloor(List<FloorEntity> list);

        void showListHistory(List<CodePalletEntity> list);
        void showDialogCreateIPAddress(String code);
//
    }

    interface Presenter extends BasePresenter {
        void getListSO(int orderType);

        void getListFloor(long orderId);

        void getListCodePallet(long orderId, int batch, int floor);

        void print(String id, String code);

        void saveIPAddress(String ipAddress, int port, String code);
    }
}
