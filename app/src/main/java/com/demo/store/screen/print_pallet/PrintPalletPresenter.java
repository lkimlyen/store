package com.demo.store.screen.print_pallet;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocketDelivery;
import com.demo.architect.domain.AddPalletUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;
import com.demo.store.manager.UserManager;
import com.demo.store.util.ConvertUtils;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintPalletPresenter implements PrintPalletContract.Presenter {

    private final String TAG = PrintPalletPresenter.class.getName();
    private final PrintPalletContract.View view;
    private final AddPalletUsecase addPalletUsecase;


    @Inject
    LocalRepository localRepository;

    @Inject
    PrintPalletPresenter(@NonNull PrintPalletContract.View view, AddPalletUsecase addPalletUsecase) {
        this.view = view;
        this.addPalletUsecase = addPalletUsecase;
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


    @Override
    public void getListCreatePalletPrint(long orderId, int batch, int floorId) {
        localRepository.getListCreatePalletPrint(orderId, batch, floorId).subscribe(new Action1<LinkedHashMap<String, List<DetailProductScanModel>>>() {
            @Override
            public void call(LinkedHashMap<String, List<DetailProductScanModel>> stringListLinkedHashMap) {
                view.showListCreatePalletPrint(stringListLinkedHashMap);
            }
        });
    }

    @Override
    public void getListUpload(long orderId, int batch, int floorId) {
           localRepository.getListCreatePalletUpload(orderId, batch, floorId).subscribe(new Action1<String>() {
            @Override
            public void call(String json) {
                addPalletUsecase.executeIO(new AddPalletUsecase.RequestValue(UserManager.getInstance().getUser().getId(),
                        json), new BaseUseCase.UseCaseCallback() {
                    @Override
                    public void onSuccess(Object successResponse) {
                        String code = ((AddPalletUsecase.ResponseValue) successResponse).getCode();
                        localRepository.updateStatusScanPallet(orderId, batch, floorId).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                print( code, orderId, batch, floorId);

                            }
                        });
                    }

                    @Override
                    public void onError(Object errorResponse) {
                        view.hideProgressBar();

                    }
                });
            }
        });
    }

    @Override
    public void print(String code, long orderId, int batch, int floorId) {
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    //view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    view.showDialogCreateIPAddress(code);
                    view.hideProgressBar();
                    return;
                }
                view.showProgressBar();
                ConnectSocketDelivery connectSocket = new ConnectSocketDelivery(address.getIpAddress(), address.getPortNumber(),
                        code, new ConnectSocketDelivery.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {

                            if (TextUtils.isEmpty(code)) {
                                localRepository.getListCreatePalletUpload(orderId, batch, floorId).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String json) {
                                        addPalletUsecase.executeIO(new AddPalletUsecase.RequestValue(UserManager.getInstance().getUser().getId(),
                                                json), new BaseUseCase.UseCaseCallback() {
                                            @Override
                                            public void onSuccess(Object successResponse) {
                                                String code = ((AddPalletUsecase.ResponseValue) successResponse).getCode();
                                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                                print(code, orderId, batch, floorId);

                                            }

                                            @Override
                                            public void onError(Object errorResponse) {
                                                view.hideProgressBar();

                                            }
                                        });
                                    }
                                });

                            } else {
                                localRepository.updateStatusScanPallet(orderId, batch, floorId).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.hideProgressBar();
                                       view.printSuccessfully();
                                    }
                                });

                            }


                        } else {
                            view.hideProgressBar();
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_connect_printer));

                        }
                    }
                });

                connectSocket.execute();
            }
        });
    }

    @Override
    public void saveIPAddress(String ipAddress, int port, long orderId, int batch, int floorId) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                print("", orderId, batch, floorId);
            }
        });
    }
}
