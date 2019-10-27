package com.demo.store.screen.history_pallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.CodePalletEntity;
import com.demo.architect.data.model.FloorEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.store.R;
import com.demo.store.adapter.HistoryAdapter;
import com.demo.store.app.base.BaseFragment;
import com.demo.store.dialogs.ChangeIPAddressDialog;
import com.demo.store.manager.TypeSOManager;
import com.demo.store.util.Precondition;
import com.demo.store.widgets.spinner.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPalletFragment extends BaseFragment implements HistoryPalletContract.View {
    private final String TAG = HistoryPalletFragment.class.getName();
    private HistoryPalletContract.Presenter mPresenter;
    //  private HistoryAdapter adapter;
    @BindView(R.id.ss_code_so)
    SearchableSpinner ssCodeSO;

    @BindView(R.id.ss_type_product)
    SearchableSpinner ssTypeProduct;

    @BindView(R.id.ss_floor)
    SearchableSpinner ssFloor;
    @BindView(R.id.ss_batch)
    SearchableSpinner ssBatch;
    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;

    @BindView(R.id.rv_history)
    RecyclerView rvHistory;

    @BindView(R.id.et_search)
    EditText etSearch;
    private long orderId = 0;
    private int floorId = 0;
    private int orderType = 0;
    private int batch = 0;
    private HistoryAdapter historyAdapter;
    private List<CodePalletEntity> list = new ArrayList<>();

    private List<CodePalletEntity> allList = new ArrayList<>();

    public HistoryPalletFragment() {
        // Required empty public constructor
    }


    public static HistoryPalletFragment newInstance() {
        HistoryPalletFragment fragment = new HistoryPalletFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode ==.REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                showSuccess(getString(R.string.text_print_success));
//            }
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_pallet, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        // Vibrate for 500 milliseconds
        ssCodeSO.setPrintStamp(true);
        ssTypeProduct.setPrintStamp(true);
        ssFloor.setPrintStamp(true);
        ssBatch.setPrintStamp(true);
        ssCodeSO.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });

        ssTypeProduct.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {

                return false;
            }
        });

        ssFloor.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });

        ssBatch.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });


        ArrayAdapter<TypeSOManager.TypeSO> adapter = new ArrayAdapter<TypeSOManager.TypeSO>(
                getContext(), android.R.layout.simple_spinner_item, TypeSOManager.getInstance().getListType());
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        ssTypeProduct.setAdapter(adapter);
        ssTypeProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.getListSO(TypeSOManager.getInstance().getValueByPositon(position));
                orderType = TypeSOManager.getInstance().getValueByPositon(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        historyAdapter = new HistoryAdapter(list, new HistoryAdapter.OnPrintListener() {
            @Override
            public void onPrint(String code) {
                mPresenter.print("", code);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.addItemDecoration(dividerItemDecoration);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setAdapter(historyAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etSearch.getText().toString())) {
                    list.clear();
                    list.addAll(allList);
                } else {
                    list.clear();
                    for (CodePalletEntity item : allList) {
                        if (item.getCountNum().contains(editable.toString()) || item.getCode().contains(editable.toString())) {
                            list.add(item);
                        }
                    }

                }

                historyAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void setPresenter(HistoryPalletContract.Presenter presenter) {
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
        showNotification(message, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void showSuccess(String message) {
        showToast(message);
    }

    @Override
    public void showListSO(List<SOEntity> list) {
        ArrayAdapter<SOEntity> adapter = new ArrayAdapter<SOEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssCodeSO.setAdapter(adapter);
        ssCodeSO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvCustomerName.setText(list.get(position).getCustomerName());
                orderId = list.get(position).getOrderId();
                initBatch(list.get(position).getTurnList());
                if (orderId > 0) {
                    mPresenter.getListFloor(orderId);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initBatch(List<SOEntity.Turn> list) {
        ArrayAdapter<SOEntity.Turn> adapter = new ArrayAdapter<SOEntity.Turn>(getContext(), android.R.layout.simple_spinner_item, list);

        ssBatch.setAdapter(adapter);
        ssBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                batch = list.get(position).getTurn();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListFloor(List<FloorEntity> list) {
        ArrayAdapter<FloorEntity> adapter = new ArrayAdapter<FloorEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssFloor.setAdapter(adapter);
        ssFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                floorId = list.get(position).getFloorId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListHistory(List<CodePalletEntity> list) {
        this.list.clear();
        this.list.addAll(list);
        this.allList.clear();
        this.allList.addAll(list);
        etSearch.setEnabled(allList.size() >0);
        historyAdapter.notifyDataSetChanged();
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();

    }

    @OnClick(R.id.bt_search)
    public void search() {
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }
        if (floorId == 0) {
            showError(getString(R.string.err_floor_is_empty));
            return;
        }
        if (batch == 0) {
            showError(getString(R.string.err_batch_is_empty));
            return;
        }
        etSearch.getText().clear();
        etSearch.setEnabled(false);
        mPresenter.getListCodePallet(orderId, batch, floorId);
    }

    @Override
    public void showDialogCreateIPAddress(String code) {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port, code);
                dialog.dismiss();
            }
        });
    }

}
