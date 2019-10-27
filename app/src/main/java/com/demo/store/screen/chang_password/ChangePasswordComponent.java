package com.demo.store.screen.chang_password;


import com.demo.store.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ChangePasswordModule.class})
public interface ChangePasswordComponent {
    void inject(ChangePasswordActivity activity);

}
