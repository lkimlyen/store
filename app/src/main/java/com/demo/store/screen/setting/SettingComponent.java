package com.demo.store.screen.setting;


import com.demo.store.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {SettingModule.class})
public interface SettingComponent {
    void inject(SettingActivity activity);

}
