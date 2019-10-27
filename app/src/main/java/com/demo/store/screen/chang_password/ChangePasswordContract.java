package com.demo.store.screen.chang_password;

import com.demo.store.app.base.BasePresenter;
import com.demo.store.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ChangePasswordContract {
    interface View extends BaseView<Presenter> {
        void changePassSuccess();
        void changeError(String error);
    }

    interface Presenter extends BasePresenter {
        void changePass(String oldPass, String newPass);

    }
}
