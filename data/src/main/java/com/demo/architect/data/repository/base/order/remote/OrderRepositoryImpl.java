package com.demo.architect.data.repository.base.order.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.SOEntity;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;

import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public class OrderRepositoryImpl implements OrderRepository {
    private final static String TAG = OrderRepositoryImpl.class.getName();

    private OrderApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public OrderRepositoryImpl(OrderApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
    }
    private void handleSOResponse(Call<BaseListResponse<SOEntity>> call, ObservableEmitter<BaseListResponse<SOEntity>> emitter) {
        try {
            BaseListResponse<SOEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleStringResponse(Call<BaseResponse<String>> call, ObservableEmitter<BaseResponse<String>> emitter) {
        try {
            BaseResponse<String> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleBaseResponse(Call<BaseResponse> call, ObservableEmitter<BaseResponse> emitter) {
        try {
            BaseResponse response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
   
    private void handleBaseListResponse(Call<BaseListResponse> call, ObservableEmitter<BaseListResponse> emitter) {
        try {
            BaseListResponse response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleIntegerResponse(Call<BaseResponse<Integer>> call, ObservableEmitter<BaseResponse<Integer>> emitter) {
        try {
            BaseResponse<Integer> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }


    @Override
    public Observable<BaseListResponse<SOEntity>> getListSO(final String key,final int orderType) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<SOEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<SOEntity>> emitter) throws Exception {
                handleSOResponse(mRemoteApiInterface.getListSO(
                        server + "/WS/api/GetSOByType",key, orderType), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<String>> addPallet(final long userId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<String>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<String>> emitter) throws Exception {
                handleStringResponse(mRemoteApiInterface.addPallet(
                        server + "/WS/api/AddPallet",userId, json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addScanRequestByPallet(final long userId, final String code) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addScanRequestByPallet(
                        server + "/WS/api/AddScanRequestByPallet",userId, code), emitter);
            }
        });
    }


}
