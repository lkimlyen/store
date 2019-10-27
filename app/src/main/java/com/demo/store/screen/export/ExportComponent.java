package com.demo.store.screen.export;


import com.demo.store.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ExportModule.class})
public interface ExportComponent {
    void inject(ExportActivity activity);

}
