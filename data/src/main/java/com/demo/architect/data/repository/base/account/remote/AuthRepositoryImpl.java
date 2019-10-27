package com.demo.architect.data.repository.base.account.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.UserResponse;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public class AuthRepositoryImpl implements AuthRepository {
    private final static String TAG = AuthRepositoryImpl.class.getName();
    private AuthApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public AuthRepositoryImpl(AuthApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;

    }

    private void handleUserLoginResponse(Call<UserResponse> call, ObservableEmitter<UserResponse> emitter) {
        try {
            UserResponse response = call.execute().body();

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
    private void handleBaseResponse(Call<BaseListResponse> call, ObservableEmitter<BaseListResponse> emitter) {
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


    private void handleStringResponse(Call<String> call, ObservableEmitter<String> emitter) {
        try {
            String response = call.execute().body();

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
    public Observable<UserResponse> login(final String key, final String username, final String password) {
        
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new ObservableOnSubscribe<UserResponse>() {
            @Override
            public void subscribe(ObservableEmitter<UserResponse> emitter) throws Exception {
                handleUserLoginResponse(mRemoteApiInterface.login(
                        server + "/WS/api/LoginWS"
                        ,key, username, password), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse> changePassword(final String userId, final String oldPass, final String newPass) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new ObservableOnSubscribe<BaseListResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.changePassWord(
                        server + "/WS/api/ChangePassWord"
                        , userId, oldPass, newPass), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse> updateSoft(final String appCode, final long userId, final String version, final int numNotUpdate, final String dateServer, final String ime) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new ObservableOnSubscribe<BaseListResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.updateSoft(
                        server + "/WS/api/UpdateSoft"
                        ,appCode, userId, version,numNotUpdate, dateServer, ime), emitter);
            }
        });
    }


    @Override
    public Observable<String> getDateServer() {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                handleStringResponse(mRemoteApiInterface.getDateServer(), emitter);
            }
        });

    }


}
