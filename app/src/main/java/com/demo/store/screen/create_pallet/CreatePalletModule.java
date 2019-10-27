package com.demo.store.screen.create_pallet;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class CreatePalletModule {
    private final CreatePalletContract.View StagesView;

    public CreatePalletModule(CreatePalletContract.View StagesView) {
        this.StagesView = StagesView;
    }

    @Provides
    @NonNull
    CreatePalletContract.View provideStagesView() {
        return this.StagesView;
    }
}

