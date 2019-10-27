package com.demo.store.screen.history_pallet;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocketDelivery;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetCodePalletUsecase;
import com.demo.architect.domain.GetListFloorUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;
import com.demo.store.manager.ListApartmentManager;
import com.demo.store.manager.ListCodePalletManager;
import com.demo.store.manager.ListSOManager;
import com.demo.store.manager.UserManager;
import com.demo.store.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPalletPresenter implements HistoryPalletContract.Presenter {

    private final String TAG = HistoryPalletPresenter.class.getName();
    private final HistoryPalletContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetListFloorUsecase getListFloorUsecase;

    private final GetCodePalletUsecase getCodePalletUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    HistoryPalletPresenter(@NonNull HistoryPalletContract.View view, GetListSOUsecase getListSOUsecase,
                           GetListFloorUsecase getListFloorUsecase, GetCodePalletUsecase getCodePalletUsecase) {
        this.view = view;

        this.getListSOUsecase = getListSOUsecase;
        this.getListFloorUsecase = getListFloorUsecase;
        this.getCodePalletUsecase = getCodePalletUsecase;
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
                        //   view.clearDataNoProduct(true);
                    }
                });
    }

    @Override
    public void getListFloor(long orderId) {
        view.showProgressBar();
        getListFloorUsecase.executeIO(new GetListFloorUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetListFloorUsecase.ResponseValue,
                        GetListFloorUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListFloorUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                     //   ListApartmentManager.getInstance().setListDepartment(successResponse.getEntity());
                        view.showListFloor(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListFloorUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListApartmentManager.getInstance().setListDepartment(new ArrayList<>());

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

                                view.showError(CoreApplication.getInstance().getString(R.string.text_no_data));
                            }
                            view.showListHistory(list);
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
    public void print(String id, String code) {
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
                        id, new ConnectSocketDelivery.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {

                            if (TextUtils.isEmpty(id)) {
                                print(code, code);

                            } else {

                                view.hideProgressBar();
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_print_success));
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
    public void saveIPAddress(String ipAddress, int port, String code) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                print("", code);
            }
        });
    }
}
