package com.demo.store.screen.history_pallet;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class HistoryPalletModule {
    private final HistoryPalletContract.View CreateCodePackageView;

    public HistoryPalletModule(HistoryPalletContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    HistoryPalletContract.View provideHistoryPackageView() {
        return this.CreateCodePackageView;
    }
}

