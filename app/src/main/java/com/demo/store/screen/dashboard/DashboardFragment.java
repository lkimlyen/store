package com.demo.store.screen.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.model.UserEntity;
import com.demo.store.R;
import com.demo.store.app.base.BaseFragment;
import com.demo.store.manager.ServerManager;
import com.demo.store.screen.create_pallet.CreatePalletActivity;
import com.demo.store.screen.export.ExportActivity;
import com.demo.store.screen.history_pallet.HistoryPalletActivity;
import com.demo.store.screen.login.LoginActivity;
import com.demo.store.screen.setting.SettingActivity;
import com.demo.store.util.Precondition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class DashboardFragment extends BaseFragment implements DashboardContract.View {
    private final String TAG = DashboardFragment.class.getName();

    @BindView(R.id.txt_name)
    TextView txtName;

    @BindView(R.id.txt_position)
    TextView txtPosition;

    @BindView(R.id.btn_link)
    Button btnLink;

    private DashboardContract.Presenter mPresenter;

    public DashboardFragment() {
        // Required empty public constructor
    }


    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        btnLink.setText(String.format(getString(R.string.text_web_report), ServerManager.getInstance().getServer()));
    }


    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.mPresenter = Precondition.checkNotNull(presenter);
    }

    @Override
    public void showProgressBar() {
        showProgressDialog();
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }


    @Override
    public void showUser(UserEntity user) {
        txtName.setText(user.getName());

    }


    @OnClick(R.id.btn_logout)
    public void logout() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_do_you_want_logout))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.logout();
                        RealmHelper.getInstance().initRealm(false);
                        LoginActivity.start(getContext());
                        getActivity().finishAffinity();
                    }
                })
                .setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_setting)
    public void setting() {
        SettingActivity.start(getContext());
    }

    @OnClick(R.id.bt_scan_pallet)
    public void scanStages() {
        CreatePalletActivity.start(getContext());
    }

    @OnClick(R.id.bt_export)
    public void export() {
        ExportActivity.start(getContext());
    }


    @OnClick(R.id.btn_link)
    public void link() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(btnLink.getText().toString()));
        startActivity(intent);
    }

    @OnClick(R.id.bt_history)
    public void history() {
        HistoryPalletActivity.start(getContext());
    }


}
