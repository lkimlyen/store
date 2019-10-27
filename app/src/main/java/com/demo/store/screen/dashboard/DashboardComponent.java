package com.demo.store.screen.dashboard;


import com.demo.store.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {DashboardModule.class})
public interface DashboardComponent {
    void inject(DashboardActivity activity);

}
