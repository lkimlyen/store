package com.demo.store.screen.export;

import android.util.Log;

import androidx.annotation.NonNull;

import com.androidnetworking.core.Core;
import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.model.FloorEntity;
import com.demo.architect.data.model.offline.ExportModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddExportPalletUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetCodePalletUsecase;
import com.demo.architect.domain.GetListFloorUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;
import com.demo.store.manager.ListCodePalletManager;
import com.demo.store.manager.ListSOManager;
import com.demo.store.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ExportPresenter implements ExportContract.Presenter {

    private final String TAG = ExportPresenter.class.getName();
    private final ExportContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetListFloorUsecase getListFloorUsecase;
    private final GetCodePalletUsecase getCodePalletUsecase;
    private final AddExportPalletUsecase addExportPalletUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    ExportPresenter(@NonNull ExportContract.View view,
                    GetListSOUsecase getListSOUsecase, GetListFloorUsecase getListFloorUsecase,
                    GetCodePalletUsecase getCodePalletUsecase, AddExportPalletUsecase addExportPalletUsecase) {
        this.view = view;
        this.getListFloorUsecase = getListFloorUsecase;
        this.getListSOUsecase = getListSOUsecase;
        this.getCodePalletUsecase = getCodePalletUsecase;
        this.addExportPalletUsecase = addExportPalletUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }


    @Override
    public void getListFloor(long orderId) {
        view.showProgressBar();
        getListFloorUsecase.executeIO(new GetListFloorUsecase.RequestValue(orderId), new BaseUseCase.UseCaseCallback() {
            @Override
            public void onSuccess(Object successResponse) {
                if (successResponse instanceof GetListFloorUsecase.ResponseValue) {
                    List<FloorEntity> list = ((GetListFloorUsecase.ResponseValue) successResponse).getEntity();
                    view.showListFloor(list);
                }
                view.hideProgressBar();
            }

            @Override
            public void onError(Object errorResponse) {
                if (errorResponse instanceof GetListFloorUsecase.ErrorValue) {
                    String error = ((GetListFloorUsecase.ErrorValue) errorResponse).getDescription();
                    view.hideProgressBar();
                    view.showError(error);
                }
            }
        });
    }

    @Override
    public void getListSO(int orderType) {
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOUsecase.RequestValue(orderType),
                new BaseUseCase.UseCaseCallback<GetListSOUsecase.ResponseValue,
                        GetListSOUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListSOUsecase.ResponseValue successResponse) {
                        view.showListSO(successResponse.getEntity());
                        ListSOManager.getInstance().setListSO(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_so_success));

                    }

                    @Override
                    public void onError(GetListSOUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListSOManager.getInstance().setListSO(new ArrayList<>());

                    }
                });
    }

    @Override
    public void getListCodePallet(long orderId, int batch, int floor) {
        view.showProgressBar();
        getCodePalletUsecase.executeIO(new GetCodePalletUsecase.RequestValue(orderId, batch, floor),
                new BaseUseCase.UseCaseCallback() {
                    @Override
                    public void onSuccess(Object successResponse) {
                        if (successResponse instanceof GetCodePalletUsecase.ResponseValue) {
                            List<CodePalletEntity> list = ((GetCodePalletUsecase.ResponseValue) successResponse).getEntity();
                            ListCodePalletManager.getInstance().setList(list);
                            if (list.size() == 0) {

                                view.showError(CoreApplication.getInstance().getString(R.string.text_code_pallet_empty));
                            }else {
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_code_pallet_success));
                            }
                            view.hideProgressBar();

                        }

                    }

                    @Override
                    public void onError(Object errorResponse) {
                        if (errorResponse instanceof GetCodePalletUsecase.ErrorValue) {
                            String error = ((GetCodePalletUsecase.ErrorValue) errorResponse).getDescription();

                            view.hideProgressBar();
                            view.showError(error);
                            ListCodePalletManager.getInstance().setList(new ArrayList<>());
                        }

                    }
                });
    }

    @Override
    public void checkBarcode(String barcode, long orderId, int batch, int floorId) {
        barcode = barcode.toUpperCase();
        CodePalletEntity entity = ListCodePalletManager.getInstance().getPalletByCode(barcode);
        if (entity == null) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        } else {
            if (entity.getStatus() == 1) {
                addCode(orderId, batch, floorId, barcode);
            } else if (entity.getStatus() == 3){
                showError(CoreApplication.getInstance().getString(R.string.err_barcode_exported));
            }else {
                showError(CoreApplication.getInstance().getString(R.string.err_barcode_cancelled));
            }
        }
    }

    @Override
    public void getListScanExport(long orderId, int batch, int floor) {
        localRepository.getListScanExport(orderId, batch, floor).subscribe(new Action1<RealmResults<ExportModel>>() {
            @Override
            public void call(RealmResults<ExportModel> exportModels) {
                view.showListExportPallet(exportModels);
            }
        });
    }

    private void addCode(long orderId, int batch, int floor, String code) {
        addExportPalletUsecase.executeIO(new AddExportPalletUsecase.RequestValue(UserManager.getInstance().getUser().getId(),
                code), new BaseUseCase.UseCaseCallback() {
            @Override
            public void onSuccess(Object successResponse) {
                localRepository.saveExportPallet(orderId, batch, floor, code).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ListCodePalletManager.getInstance().updateStatusPallet(code);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_eport_pallet_successfully));
                    }
                });
            }

            @Override
            public void onError(Object errorResponse) {
                String error = ((GetCodePalletUsecase.ErrorValue) errorResponse).getDescription();

                view.hideProgressBar();
                view.showError(error);
            }
        });
    }

}
