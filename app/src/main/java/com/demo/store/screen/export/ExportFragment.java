package com.demo.store.screen.export;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.FloorEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.CreatePalletModel;
import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.architect.data.model.offline.ExportModel;
import com.demo.architect.data.model.offline.ProductListModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.utils.view.DateUtils;
import com.demo.store.R;
import com.demo.store.adapter.CreatePalletAdapter;
import com.demo.store.adapter.ExportAdapter;
import com.demo.store.app.base.BaseFragment;
import com.demo.store.manager.TypeSOManager;
import com.demo.store.screen.print_pallet.PrintPalletActivity;
import com.demo.store.util.Precondition;
import com.demo.store.widgets.barcodereader.BarcodeScannerActivity;
import com.demo.store.widgets.spinner.SearchableListDialog;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public class ExportFragment extends BaseFragment implements ExportContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = ExportFragment.class.getName();
    private ExportContract.Presenter mPresenter;
    //private DetailProductAdapter adapter;
    public MediaPlayer mp1, mp2, mp3;
    private int batch = 0;
    private int typeProduct;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.tv_batch)
    TextView tvBatch;

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;

    @BindView(R.id.et_barcode)
    EditText edtBarcode;

    @BindView(R.id.rv_scan)
    RecyclerView rvScan;

    @BindView(R.id.tv_type_product)
    TextView tvTypeProduct;


    @BindView(R.id.tv_created_date)
    TextView tvDateScan;

    @BindView(R.id.tv_floor)
    TextView tvFloor;
    private Vibrator vibrate;
    private long orderId = 0;
    private int floorId = 0;
    private boolean init;
    private int typeScan = 0;

    public ExportFragment() {
        // Required empty public constructor
    }


    public static ExportFragment newInstance() {
        ExportFragment fragment = new ExportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 178) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("Message");
                showSuccess(result);
                return;
            }
        }

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.KEY_CAPTURED_BARCODE);

                String contents = barcode.rawValue;
                String barcode2 = contents.replace("DEMO", "");
                Log.d(TAG, barcode2);
                mPresenter.checkBarcode(barcode2, orderId, batch, floorId);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        mp3 = MediaPlayer.create(getActivity(), R.raw.beepdup);
        initView();
        return view;
    }


    private void initView() {

        rvScan.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        rvScan.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        rvScan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvScan.addItemDecoration(dividerItemDecoration);
        tvDateScan.setText(DateUtils.getShortDateCurrent());
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
    }

    @Override
    public void setPresenter(ExportContract.Presenter presenter) {
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
        showToast(message);
    }

    @Override
    public void showListFloor(List<FloorEntity> list) {
        if (list.size() > 0) {
            tvFloor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (adapter != null &&adapter.getItemCount() > 0) {
//                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText(CoreApplication.getInstance().getString(R.string.text_title_noti))
//                                .setContentText(CoreApplication.getInstance().getString(R.string.text_back_have_detail_waiting))
//                                .setConfirmText(CoreApplication.getInstance().getString(R.string.text_yes))
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                        sweetAlertDialog.dismiss();
//                                        mPresenter.uploadData(orderId);
//                                    }
//                                })
//                                .setCancelText(CoreApplication.getInstance().getString(R.string.text_no))
//                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        mPresenter.deleteAllData();
//                                        sweetAlertDialog.dismiss();
//                                        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
//                                                (list);
//                                        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
//                                            @Override
//                                            public void onSearchableItemClicked(Object item, int position) {
//                                                FloorEntity departmentEntity = (FloorEntity) item;
//                                                tvFloor.setText(departmentEntity.getFloorName());
//                                                floorId = departmentEntity.getFloorId();
//                                                tvBatch.setText(getString(R.string.text_choose_times_scan));
//                                                batch = 0;
//
//                                            }
//                                        });
//                                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
//
//                                    }
//                                })
//                                .show();
//                    } else {
                    SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                            (list);
                    searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                        @Override
                        public void onSearchableItemClicked(Object item, int position) {
                            FloorEntity departmentEntity = (FloorEntity) item;
                            tvFloor.setText(departmentEntity.getFloorName());
                            floorId = departmentEntity.getFloorId();

                            tvBatch.setText(getString(R.string.text_choose_batch_scan));
                            batch = 0;

                        }
                    });
                    searchableListDialog.show(getActivity().getFragmentManager(), TAG);
//                    }

                }
            });
        } else {
            tvFloor.setOnClickListener(null);
        }
    }

    @Override
    public void showListExportPallet(RealmResults<ExportModel> parent) {
        ExportAdapter adapter = new ExportAdapter(parent);
        rvScan.setAdapter(adapter);
    }

    @Override
    public void showListSO(List<SOEntity> list) {
        if (list.size() > 0) {
            tvCodeSO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (adapter != null &&adapter.getItemCount() > 0) {
//                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText(CoreApplication.getInstance().getString(R.string.text_title_noti))
//                                .setContentText(CoreApplication.getInstance().getString(R.string.text_back_have_detail_waiting))
//                                .setConfirmText(CoreApplication.getInstance().getString(R.string.text_yes))
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                        sweetAlertDialog.dismiss();
//                                        mPresenter.uploadData(orderId);
//                                    }
//                                })
//                                .setCancelText(CoreApplication.getInstance().getString(R.string.text_no))
//                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        mPresenter.deleteAllData();
//                                        sweetAlertDialog.dismiss();
//                                        setListSO(list);
//
//                                    }
//                                })
//                                .show();
//                    } else {
                    setListSO(list);
//                    }

                }

            });
        } else {
            tvCodeSO.setOnClickListener(null);
        }

    }

    void setListSO(List<SOEntity> listSO) {
        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                (listSO);
        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                SOEntity soItem = (SOEntity) item;

                List<SOEntity.Turn> turnList = soItem.getTurnList();
                tvBatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setListTurn(turnList);
                    }
                });
                tvCodeSO.setText(soItem.getCodeSO());
                tvCustomerName.setText(soItem.getCustomerName());
                orderId = soItem.getOrderId();
                tvBatch.setText(getString(R.string.text_choose_batch_scan));
                batch = 0;
                tvFloor.setText(getString(R.string.text_choose_floor_print));
                floorId = 0;
                mPresenter.getListFloor(orderId);
            }
        });
        searchableListDialog.show(getActivity().getFragmentManager(), TAG);

    }

    void setListTurn(List<SOEntity.Turn> list) {
        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                (list);
        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                SOEntity.Turn turn = (SOEntity.Turn) item;
                batch = turn.getTurn();
                tvBatch.setText(turn.getTurn() + "");

                mPresenter.getListCodePallet(orderId, turn.getTurn(),floorId);

                mPresenter.getListScanExport(orderId, batch,floorId);
            }
        });
        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
    }


    @Override
    public void startMusicError() {
        mp2.start();
    }

    @Override
    public void startMusicSuccess() {
        mp1.start();
    }

    @Override
    public void turnOnVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrate.vibrate(500);
        }
    }



    @OnClick(R.id.ic_refresh)
    public void refresh() {
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @OnClick(R.id.bt_save)
    public void save() {
        edtBarcode.onEditorAction(EditorInfo.IME_ACTION_DONE);
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
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString().trim(), orderId, batch,floorId);
                        sweetAlertDialog.dismiss();
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


    @OnClick(R.id.img_back)
    public void back() {
//        if (adapter != null && adapter.getItemCount() > 0) {
//            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(getString(R.string.text_title_noti))
//                    .setContentText(getString(R.string.text_back_have_detail_waiting))
//                    .setConfirmText(getString(R.string.text_yes))
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            // mPresenter.deleteAllItemLog();
//                            mPresenter.uploadData(orderId);
//                            sweetAlertDialog.dismiss();
//                        }
//                    })
//                    .setCancelText(getString(R.string.text_no))
//                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            sweetAlertDialog.dismiss();
//                            getActivity().finish();
//
//                        }
//                    })
//                    .show();
//
//        } else {
        getActivity().finish();
//        }
    }


    @OnClick(R.id.bt_scan)
    public void scan() {
        edtBarcode.onEditorAction(EditorInfo.IME_ACTION_DONE);
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


        Intent launchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
        getActivity().startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);

    }

    @OnClick(R.id.tv_type_product)
    public void chooseProduct() {

//        if (adapter != null && adapter.getItemCount() > 0) {
//            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(CoreApplication.getInstance().getString(R.string.text_title_noti))
//                    .setContentText(CoreApplication.getInstance().getString(R.string.text_back_have_detail_waiting))
//                    .setConfirmText(CoreApplication.getInstance().getString(R.string.text_yes))
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                            sweetAlertDialog.dismiss();
//                            mPresenter.uploadData(orderId);
//                        }
//                    })
//                    .setCancelText(CoreApplication.getInstance().getString(R.string.text_no))
//                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            mPresenter.deleteAllData();
//                            sweetAlertDialog.dismiss();
//                            SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
//                                    (TypeSOManager.getInstance().getListType());
//                            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
//                                @Override
//                                public void onSearchableItemClicked(Object item, int position) {
//                                    TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
//                                    tvTypeProduct.setText(typeSO.getName());
//                                    typeProduct = typeSO.getValue();
//                                    tvBatch.setText(getString(R.string.text_choose_times_scan));
//                                    batch = 0;
//                                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
//                                    orderId = 0;
//                                    tvFloor.setText(getString(R.string.text_choose_receiving_department));
//                                    floorId = 0;
//                                    tvCustomerName.setText("");
//                                    mPresenter.getListSO(typeSO.getValue());
//                                }
//                            });
//                            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
//
//                        }
//                    })
//                    .show();
//        } else {
        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                (TypeSOManager.getInstance().getListType());
        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
                tvTypeProduct.setText(typeSO.getName());
                typeProduct = typeSO.getValue();
                tvCodeSO.setText(getString(R.string.text_choose_code_so));
                orderId = 0;
                tvBatch.setText(getString(R.string.text_choose_batch_scan));
                batch = 0;
                tvFloor.setText(getString(R.string.text_choose_floor_print));
                floorId = 0;
                tvCustomerName.setText("");
                mPresenter.getListSO(typeSO.getValue());
            }
        });
        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
//        }


    }

}
