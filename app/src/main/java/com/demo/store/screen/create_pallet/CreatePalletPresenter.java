package com.demo.store.screen.create_pallet;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.model.FloorEntity;
import com.demo.architect.data.model.ProductListEntity;
import com.demo.architect.data.model.offline.CreatePalletModel;
import com.demo.architect.data.model.offline.ProductListModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetListFloorUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetProductForPalletUsecase;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;
import com.demo.store.manager.ListProductManager;
import com.demo.store.manager.ListSOManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreatePalletPresenter implements CreatePalletContract.Presenter {

    private final String TAG = CreatePalletPresenter.class.getName();
    private final CreatePalletContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetListFloorUsecase getListFloorUsecase;
    private final GetProductForPalletUsecase getProductForPalletUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    CreatePalletPresenter(@NonNull CreatePalletContract.View view,
                          GetListSOUsecase getListSOUsecase, GetListFloorUsecase getListFloorUsecase,
                          GetProductForPalletUsecase getProductForPalletUsecase) {
        this.view = view;
        this.getListFloorUsecase = getListFloorUsecase;
        this.getListSOUsecase = getListSOUsecase;
        this.getProductForPalletUsecase = getProductForPalletUsecase;
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

    private boolean allowedToSave = true;


    private int numberLoop = 0;
    private int countProductNull = 0;


    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }


    @Override
    public void deleteScanPallet(long id, long detaiId) {
        view.showProgressBar();
        localRepository.deleteScanPallet(id, detaiId).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
            }
        });
    }

    @Override
    public void uploadData(long orderId) {
        view.showProgressBar();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();

    }

    @Override
    public void saveBarcodeResidual(ProductListModel productListModel, ProductModel productModel, final int batch, String barcode) {
        localRepository.saveBarcodeResidual(productListModel, productModel, batch, barcode).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
            }
        });
    }

//    @Override
//    public void saveBarcodeToDataBase(int times, ProductDetail
//            productDetail, int number, int departmentId, GroupEntity groupEntity, boolean typeScan, boolean residual) {
//        view.showProgressBar();
//        UserEntity user = UserManager.getInstance().getUser();
//        String groupCode = null;
//        long groupCodeId = -1;
//        if (groupEntity != null) {
//            groupCode = groupEntity.getGroupCode();
//            groupCodeId = groupEntity.getMasterGroupId();
//        }
//        LogScanStages logScanStages = new LogScanStages(productDetail.getOrderId(), departmentId, user.getRole(), productDetail.getProductDetailId(),
//                groupCodeId, groupCode, productDetail.getBarcode(), productDetail.getModule(), number, typeScan, times,
//                ConvertUtils.getDateTimeCurrent(), user.getId(), number, Constants.WAITING_UPLOAD);
//        localRepository.addLogScanStagesAsync(logScanStages, productDetail.getId()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                if (!residual) {
//                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
//                    view.startMusicSuccess();
//                } else {
//                    view.showCheckResidual();
//                }
//                view.turnOnVibrator();
//                view.hideProgressBar();
//                view.refreshLayout();
//
//            }
//        });
//
//    }


    @Override
    public void updateNumberScan(long masterId, long id, int number) {
        view.showProgressBar();
        localRepository.updateNumberScanCreatePallet(masterId, id, number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                view.hideProgressBar();

            }
        });
    }

    @Override
    public void getListScanPallet(long orderId, int batch) {
        localRepository.getListScanPallet(orderId, batch).subscribe(new Action1<RealmResults<CreatePalletModel>>() {
            @Override
            public void call(RealmResults<CreatePalletModel> createPalletModels) {
                view.showListScanPallet(createPalletModels);
            }
        });
    }

    @Override
    public void checkEnoughPack(long orderId, int floorId, int batch) {
        localRepository.checkEnoughPackPrint(orderId, floorId, batch).subscribe(new Action1<HashMap<Boolean, Integer>>() {
            @Override
            public void call(HashMap<Boolean, Integer> maps) {
                for ( Map.Entry<Boolean,Integer> item : maps.entrySet()){
                    if (item.getKey()){
                        if (item.getValue() > 0){
                            view.showWarningPrint();
                        }else {
                            view.goToPrint();
                        }
                    }else {
                        if (item.getValue() == 0){
                            view.showError(CoreApplication.getInstance().getString(R.string.err_scan_pack_empty));
                        }else {
                            view.showWarningPrint();
                        }
                    }
                }


            }
        });
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
    public void getListProduct(long orderId, int batch) {
        view.showProgressBar();
        getProductForPalletUsecase.executeIO(new GetProductForPalletUsecase.RequestValue(orderId, batch),
                new BaseUseCase.UseCaseCallback() {
                    @Override
                    public void onSuccess(Object successResponse) {
                        if (successResponse instanceof GetProductForPalletUsecase.ResponseValue) {
                            List<ProductListEntity> list = ((GetProductForPalletUsecase.ResponseValue) successResponse).getEntity();
                            if (list.size() > 0) {
                                localRepository.addProductForPallet(list, batch).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.hideProgressBar();
                                    }
                                });
                            } else {
                                view.showError(CoreApplication.getInstance().getString(R.string.text_product_empty));
                            }
                            view.hideProgressBar();

                        }

                    }

                    @Override
                    public void onError(Object errorResponse) {
                        if (errorResponse instanceof GetProductForPalletUsecase.ErrorValue) {
                            String error = ((GetProductForPalletUsecase.ErrorValue) errorResponse).getDescription();
                            ListProductManager.getInstance().setListProduct(new ArrayList<>());
                            view.hideProgressBar();
                            view.showError(error);
                        }

                    }
                });
    }

    @Override
    public void checkBarcode(String barcode, long orderId, int batch) {
        barcode = barcode.toUpperCase();
        String packCode = barcode.substring(barcode.length()-5);
        int numberPack = Integer.parseInt(packCode.substring(packCode.lastIndexOf("/") + 1));
        String finalBarcode = barcode;
        localRepository.checkBarcodeScan(barcode, orderId, batch).subscribe(new Action1<List<HashMap<ProductListModel, ProductModel>>>() {
            @Override
            public void call(List<HashMap<ProductListModel, ProductModel>> result) {
                if (result == null) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                } else if (result.size() == 0) {
                    showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
                } else {
                    for (Map.Entry<ProductListModel, ProductModel> map : result.get(0).entrySet()) {
                        int numberTotal = map.getKey().getNumber() * map.getValue().getNumber() * numberPack;
                        if (numberTotal - map.getValue().getScanned() == 1) {
                            view.showDialogWarningResidual(map.getKey(), map.getValue(), finalBarcode);
                        } else {
                            showError(CoreApplication.getInstance().getString(R.string.text_product_scanned_enough));
                        }
                    }

                }
            }
        });
    }

}
