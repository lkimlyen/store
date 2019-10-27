package com.demo.architect.data.repository.base.other.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.FloorEntity;

import java.io.File;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public class OtherRepositoryImpl implements OtherRepository {
    private final static String TAG = OtherRepositoryImpl.class.getName();

    private OtherApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public OtherRepositoryImpl(OtherApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
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



    private void handleFloorResponse(Call<BaseListResponse<FloorEntity>> call, ObservableEmitter<BaseListResponse<FloorEntity>> emitter) {
        try {
            BaseListResponse<FloorEntity> response = call.execute().body();

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
    public Observable<BaseListResponse<FloorEntity>> getListFloor(final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<FloorEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<FloorEntity>> emitter) throws Exception {
                handleFloorResponse(mRemoteApiInterface.getListFloor(
                        server + "/WS/api/GetFloor",orderId), emitter);
            }
        });
    }


}
