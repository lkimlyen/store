package com.demo.store.screen.history_pallet;


import com.demo.store.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {HistoryPalletModule.class})
public interface HistoryPalletComponent {
    void inject(HistoryPalletActivity activity);

}
