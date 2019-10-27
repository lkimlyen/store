package com.demo.store.screen.login;

import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;
import com.demo.store.manager.ServerManager;
import com.demo.store.manager.UserManager;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final String TAG = LoginPresenter.class.getName();
    private final LoginContract.View view;
    private final LoginUsecase loginUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    LoginPresenter(@NonNull LoginContract.View view,
                   LoginUsecase loginUsecase) {
        this.view = view;
        this.loginUsecase = loginUsecase;
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
    public void login(String username, String password) {

        view.showProgressBar();
        loginUsecase.executeIO(new LoginUsecase.RequestValue(username, password), new BaseUseCase.UseCaseCallback
                <LoginUsecase.ResponseValue, LoginUsecase.ErrorValue>() {
            @Override
            public void onSuccess(LoginUsecase.ResponseValue successResponse) {
                Log.d(TAG, new Gson().toJson(successResponse.getEntity()));
                //Save user entity to shared preferences
                UserManager.getInstance().setUser(successResponse.getEntity());
                view.hideProgressBar();
                view.startDashboardActivity();




            }

            @Override
            public void onError(LoginUsecase.ErrorValue errorResponse) {
                view.hideProgressBar();
                String error = "";
                if (errorResponse.getDescription().contains(
                        CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                    error = CoreApplication.getInstance().getString(R.string.text_error_network);
                } else {
                    error = errorResponse.getDescription();
                }
                view.loginError(error);
            }
        });
    }

    @Override
    public void saveServer(String server) {
        ServerManager.getInstance().setServer(server);
    }


}
