package com.demo.store.screen.login;

import com.demo.store.app.base.BasePresenter;
import com.demo.store.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void loginError(String content);

        void startDashboardActivity();
    }

    interface Presenter extends BasePresenter {
        void login(String phone, String password);

        //lưu server đã chọn
        void saveServer(String server);

    }
}
