package com.demo.store.screen.print_pallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.demo.store.R;
import com.demo.store.app.CoreApplication;
import com.demo.store.app.base.BaseActivity;
import com.demo.store.app.di.Precondition;
import com.demo.store.constants.Constants;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintPalletActivity extends BaseActivity {
    @Inject
    PrintPalletPresenter PrintPalletPresenter;

    PrintPalletFragment fragment;

    public static void start(Activity activity, long orderId, String codeSO, String cusName, int floorId, String floorName, int batch) {
        Intent intent = new Intent(activity, PrintPalletActivity.class);
        intent.putExtra(Constants.KEY_ORDER_ID,orderId);
        intent.putExtra(Constants.KEY_CODE_SO, codeSO);
        intent.putExtra(Constants.KEY_CUSTOMER_NAME, cusName);
        intent.putExtra(Constants.KEY_FLOOR_ID, floorId);
        intent.putExtra(Constants.KEY_FLOOR_NAME, floorName);
        intent.putExtra(Constants.KEY_BATCH, batch);
        activity.startActivityForResult(intent,407);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new PrintPalletModule(fragment))
                .inject(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    private void initFragment() {
        fragment = (PrintPalletFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = PrintPalletFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(PrintPalletFragment fragment, int frameId) {
        Precondition.checkNotNull(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        fragment.back();
        // super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
