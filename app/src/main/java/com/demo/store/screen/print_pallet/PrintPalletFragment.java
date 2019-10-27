package com.demo.store.screen.print_pallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.architect.utils.view.DateUtils;
import com.demo.store.R;
import com.demo.store.adapter.PrintPalletAdapter;
import com.demo.store.app.base.BaseFragment;
import com.demo.store.constants.Constants;
import com.demo.store.dialogs.ChangeIPAddressDialog;
import com.demo.store.util.Precondition;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintPalletFragment extends BaseFragment implements PrintPalletContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = PrintPalletFragment.class.getName();
    private PrintPalletContract.Presenter mPresenter;
    //private DetailProductAdapter adapter;
    public MediaPlayer mp1, mp2, mp3;
    private int batch = 0;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.tv_batch)
    TextView tvBatch;

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;

    @BindView(R.id.ll_data)
    LinearLayout llData;

    @BindView(R.id.tv_total)
    TextView tvTotal;


    @BindView(R.id.tv_date_create)
    TextView tvDateScan;

    @BindView(R.id.tv_floor)
    TextView tvFloor;
    private long orderId = 0;
    private int floorId = 0;
    private int total;

    public PrintPalletFragment() {
        // Required empty public constructor
    }


    public static PrintPalletFragment newInstance() {
        PrintPalletFragment fragment = new PrintPalletFragment();
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
        View view = inflater.inflate(R.layout.fragment_print_pallet, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }


    private void initView() {
        orderId = getActivity().getIntent().getLongExtra(Constants.KEY_ORDER_ID, 0);
        floorId = getActivity().getIntent().getIntExtra(Constants.KEY_FLOOR_ID, 0);
        batch = getActivity().getIntent().getIntExtra(Constants.KEY_BATCH, 0);
        tvFloor.setText(getActivity().getIntent().getStringExtra(Constants.KEY_FLOOR_NAME));
        tvCustomerName.setText(getActivity().getIntent().getStringExtra(Constants.KEY_CUSTOMER_NAME));
        tvCodeSO.setText(getActivity().getIntent().getStringExtra(Constants.KEY_CODE_SO));
        tvBatch.setText(String.valueOf(batch));
        mPresenter.getListCreatePalletPrint(orderId, batch, floorId);
        tvDateScan.setText(DateUtils.getShortDateCurrent());
    }

    @Override
    public void setPresenter(PrintPalletContract.Presenter presenter) {
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
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void showNotification(String content, int type) {
        new SweetAlertDialog(getContext(), type)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(content)
                .setConfirmText(getString(R.string.text_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.text_title_noti));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.text_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }


    @Override
    public void showSuccess(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showListCreatePalletPrint(LinkedHashMap<String, List<DetailProductScanModel>> list) {
        total = 0;
        for (Map.Entry<String, List<DetailProductScanModel>> map : list.entrySet()) {
            addData(map.getKey(), map.getValue());
            for (DetailProductScanModel detail : map.getValue()) {
                total += detail.getNumber();
            }
        }
        tvTotal.setText(total+"");
    }

    @Override
    public void showDialogCreateIPAddress(String code) {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port,
                         orderId, batch, floorId);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void printSuccessfully() {

        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    private void addData(String apartmentName, List<DetailProductScanModel> list) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_create_pallet, null, false);
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(apartmentName);

        RecyclerView rvData = view.findViewById(R.id.rv_data);
        PrintPalletAdapter adapter = new PrintPalletAdapter(list);
        rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        rvData.setAdapter(adapter);
        llData.addView(view);

    }


    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.bt_print)
    public void print() {

        mPresenter.print("",orderId, batch, floorId);
    }

}
