package com.demo.store.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.demo.store.R;
import com.demo.store.app.bus.MainThreadBus;
import com.demo.store.app.di.component.ApplicationComponent;
import com.demo.store.app.di.component.DaggerApplicationComponent;
import com.demo.store.app.di.module.ApplicationModule;
import com.demo.store.app.di.module.NetModule;
import com.demo.store.app.di.module.UseCaseModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by uyminhduc on 12/16/16.
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CoreApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private Tracker mTracker;
    private static CoreApplication sInstance;
    private String TAG = CoreApplication.class.getSimpleName();
    private static Bus bus;

    public static CoreApplication getInstance() {
        return sInstance;
    }

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        sInstance = this;

        initializeCrashAnalytics();
        initializeFirebase();
        initializeDagger();
        initializeRealm();
        initializeCalligraphy();
        initializeEventBus();

    }

    private void initializeEventBus() {
        bus = new MainThreadBus(ThreadEnforcer.ANY);
    }

    public static void postEvent(Object event) {
        bus.post(event);
    }

    public static void registerForEvents(Object o) {
        bus.register(o);
    }

    public static void unregisterForEvents(Object o) {
        bus.unregister(o);
    }

    private void initializeCrashAnalytics() {
        Fabric.with(this, new Crashlytics());
    }

    private void initializeFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = getString(R.string.text_email);
        String password = getString(R.string.text_password);
        auth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            Log.d(TAG, "Login fail");
                        } else {
                            Log.d(TAG, "Success");
                        }
                    }
                });

        //  String token = FirebaseInstanceId.getInstance().getToken();
        // Log.d(TAG, "Core FCM Token: " + token);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initializeMultiDex();
    }

    private void initializeMultiDex() {
        MultiDex.install(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    private void initializeRealm() {
        Realm.init(this);

    }

    public void initializeDagger() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .netModule(new NetModule("http://google.com"))
                .useCaseModule(new UseCaseModule())
                .build();

        this.applicationComponent.inject(this);
    }

    private void initializeCalligraphy() {

    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
