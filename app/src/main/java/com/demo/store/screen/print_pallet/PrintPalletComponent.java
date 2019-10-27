package com.demo.store.screen.print_pallet;


import com.demo.store.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {PrintPalletModule.class})
public interface PrintPalletComponent {
    void inject(PrintPalletActivity activity);

}
