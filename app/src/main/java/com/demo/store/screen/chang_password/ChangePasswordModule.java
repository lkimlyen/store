package com.demo.store.screen.chang_password;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ChangePasswordModule {
    private final ChangePasswordContract.View ChangePasswordContractView;

    public ChangePasswordModule(ChangePasswordContract.View ChangePasswordContractView) {
        this.ChangePasswordContractView = ChangePasswordContractView;
    }

    @Provides
    @NonNull
    ChangePasswordContract.View provideChangePasswordContractView() {
        return this.ChangePasswordContractView;
    }
}

