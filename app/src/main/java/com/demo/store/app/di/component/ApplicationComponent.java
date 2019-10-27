package com.demo.store.app.di.component;


import com.demo.store.app.CoreApplication;
import com.demo.store.app.base.BaseActivity;
import com.demo.store.app.base.BaseFragment;
import com.demo.store.app.di.module.ApplicationModule;
import com.demo.store.app.di.module.NetModule;
import com.demo.store.app.di.module.RepositoryModule;
import com.demo.store.app.di.module.UseCaseModule;
import com.demo.store.screen.chang_password.ChangePasswordComponent;
import com.demo.store.screen.chang_password.ChangePasswordModule;
import com.demo.store.screen.create_pallet.CreatePalletComponent;
import com.demo.store.screen.create_pallet.CreatePalletModule;
import com.demo.store.screen.dashboard.DashboardComponent;
import com.demo.store.screen.dashboard.DashboardModule;
import com.demo.store.screen.export.ExportActivity;
import com.demo.store.screen.export.ExportComponent;
import com.demo.store.screen.export.ExportModule;
import com.demo.store.screen.history_pallet.HistoryPalletComponent;
import com.demo.store.screen.history_pallet.HistoryPalletModule;
import com.demo.store.screen.login.LoginComponent;
import com.demo.store.screen.login.LoginModule;
import com.demo.store.screen.print_pallet.PrintPalletComponent;
import com.demo.store.screen.print_pallet.PrintPalletModule;
import com.demo.store.screen.setting.SettingComponent;
import com.demo.store.screen.setting.SettingModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by uyminhduc on 12/16/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        NetModule.class,
        UseCaseModule.class,
        RepositoryModule.class})
public interface ApplicationComponent {

    void inject(CoreApplication application);

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

    LoginComponent plus(LoginModule module);


    DashboardComponent plus(DashboardModule module);

    SettingComponent plus(SettingModule module);

    ChangePasswordComponent plus(ChangePasswordModule module);


    CreatePalletComponent plus(CreatePalletModule module);

    HistoryPalletComponent plus(HistoryPalletModule module);

    PrintPalletComponent plus(PrintPalletModule module);

    ExportComponent plus(ExportModule module);
}
