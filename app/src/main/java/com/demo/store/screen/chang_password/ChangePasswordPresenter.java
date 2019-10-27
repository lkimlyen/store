package com.demo.store.screen.chang_password;

import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.ChangePasswordUsecase;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;
import com.demo.store.manager.UserManager;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {

    private final String TAG = ChangePasswordPresenter.class.getName();
    private final ChangePasswordContract.View view;
    private final ChangePasswordUsecase changePasswordUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    ChangePasswordPresenter(@NonNull ChangePasswordContract.View view,
                            ChangePasswordUsecase changePasswordUsecase) {
        this.view = view;
        this.changePasswordUsecase = changePasswordUsecase;
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
    public void changePass(String oldPass, String newPass) {
        view.showProgressBar();
        long userId = UserManager.getInstance().getUser().getId();
        changePasswordUsecase.executeIO(new ChangePasswordUsecase.RequestValue(userId, oldPass, newPass),
                new BaseUseCase.UseCaseCallback<ChangePasswordUsecase.ResponseValue,
                        ChangePasswordUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(ChangePasswordUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.changePassSuccess();
                    }

                    @Override
                    public void onError(ChangePasswordUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        String error = "";
                        if(errorResponse.getDescription().contains(
                                CoreApplication.getInstance().getString(R.string.text_error_network_host))){
                            error = CoreApplication.getInstance().getString(R.string.text_error_network);
                        }else {
                            error = errorResponse.getDescription();
                        }
                        view.changeError(error);
                    }
                });
    }
}
