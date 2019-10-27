package com.demo.store.screen.export;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ExportModule {
    private final ExportContract.View StagesView;

    public ExportModule(ExportContract.View StagesView) {
        this.StagesView = StagesView;
    }

    @Provides
    @NonNull
    ExportContract.View provideStagesView() {
        return this.StagesView;
    }
}

