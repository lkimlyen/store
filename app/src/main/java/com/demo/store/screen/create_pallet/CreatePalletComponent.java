package com.demo.store.screen.create_pallet;


import com.demo.store.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {CreatePalletModule.class})
public interface CreatePalletComponent {
    void inject(CreatePalletActivity activity);

}
