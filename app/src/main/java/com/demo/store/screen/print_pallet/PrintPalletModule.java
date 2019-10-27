package com.demo.store.screen.print_pallet;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class PrintPalletModule {
    private final PrintPalletContract.View StagesView;

    public PrintPalletModule(PrintPalletContract.View StagesView) {
        this.StagesView = StagesView;
    }

    @Provides
    @NonNull
    PrintPalletContract.View provideStagesView() {
        return this.StagesView;
    }
}

