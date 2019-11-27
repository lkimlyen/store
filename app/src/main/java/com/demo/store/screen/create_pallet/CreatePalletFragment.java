package com.demo.store.screen.create_pallet;

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
import com.demo.architect.data.model.offline.ProductListModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.utils.view.DateUtils;
import com.demo.store.R;
import com.demo.store.adapter.CreatePalletAdapter;
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

public class CreatePalletFragment extends BaseFragment implements CreatePalletContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = CreatePalletFragment.class.getName();
    private CreatePalletContract.Presenter mPresenter;
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
    private CreatePalletAdapter adapter;

    public CreatePalletFragment() {
        // Required empty public constructor
    }


    public static CreatePalletFragment newInstance() {
        CreatePalletFragment fragment = new CreatePalletFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 407) {

                showSuccess(getString(R.string.text_print_success));
                return;

            }


            if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.KEY_CAPTURED_BARCODE);

                String contents = barcode.rawValue;
                String barcode2 = contents.replace("DEMO", "");
                String codeSO = tvCodeSO.getText().toString().substring(0, 2) + tvCodeSO.getText().toString().substring(5);
                if (barcode2.contains(codeSO)) {
                    mPresenter.checkBarcode(barcode2, orderId, batch);
                } else {
                    showError(getString(R.string.text_barcode_no_exist));
                    startMusicError();
                    turnOnVibrator();
                }
                Log.d(TAG, barcode2);

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_pallet, container, false);
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
    public void setPresenter(CreatePalletContract.Presenter presenter) {
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
//                                                tvBatch.setText(getString(R.string.text_choose_batch_scan));
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
    public void showListScanPallet(RealmResults<CreatePalletModel> parent) {
        adapter = new CreatePalletAdapter(parent, new CreatePalletAdapter.OnItemListener() {
            @Override
            public void onItemClick(long id, long detailId) {
                mPresenter.deleteScanPallet(id, detailId);
            }

            @Override
            public void onEditTextChange(long id, long detailId, int number) {
                mPresenter.updateNumberScan(id, detailId, number);
            }

            @Override
            public void errorListener(String message) {
                showError(message);
            }
        });
        rvScan.setAdapter(adapter);
    }

//
//    @Override
//    public void showListLogScanStages(RealmResults<LogScanStages> parent) {

//        adapter = new DetailProductAdapter(parent, new DetailProductAdapter.OnItemClearListener() {
//            @Override
//            public void onItemClick(LogScanStages item) {
//                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText(getString(R.string.text_title_noti))
//                        .setContentText(getString(R.string.text_delete_code))
//                        .setConfirmText(getString(R.string.text_yes))
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismiss();
//                                mPresenter.deleteScanPallet(item.getId());
//
//
//                            }
//                        })
//                        .setCancelText(getString(R.string.text_no))
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismiss();
//
//                            }
//                        })
//                        .show();
//
//            }
//        }, new DetailProductAdapter.OnEditTextChangeListener() {
//            @Override
//            public void onEditTextChange(LogScanStages item, int number) {
//                if (item.getTypeScan()) {
//                    mPresenter.updateNumberScan(item.getId(), number, true);
//                } else {
//                    mPresenter.updateNumberScanInGroup(item, number);
//                }
//
//            }
//        }, new DetailProductAdapter.onErrorListener() {
//            @Override
//            public void errorListener(LogScanStages item, int numberInput, String message) {
//                if (!TextUtils.isEmpty(message)) {
//                    showToast(message);
//                    turnOnVibrator();
//                    startMusicError();
//                } else {
//                    if (item.getTypeScan()) {
//                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText(getString(R.string.text_title_noti))
//                                .setContentText(getString(R.string.text_exceed_the_number_of_requests))
//                                .setConfirmText(getString(R.string.text_yes))
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                        mPresenter.updateNumberScan(item.getId(), numberInput, true);
//                                        sweetAlertDialog.dismiss();
//
//                                    }
//                                })
//                                .setCancelText(getString(R.string.text_no))
//                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        mPresenter.updateNumberScan(item.getId(), item.getNumberInput(), false);
//                                        sweetAlertDialog.dismiss();
//
//                                    }
//                                })
//                                .show();
//                    } else {
//                        mPresenter.updateNumberScanInGroup(item, numberInput);
//                    }
//
//                }
//
//            }
//        }, new DetailProductAdapter.onOpenDetailListener() {
//            @Override
//            public void onOpenDetail(String groupCode) {
//                DetailGroupDialog detailGroupDialog = new DetailGroupDialog();
//                detailGroupDialog.show(getActivity().getFragmentManager(), TAG);
//                detailGroupDialog.setGroupCode(groupCode);
//            }
//        }, new DetailProductAdapter.onClickEditTextListener() {
//            @Override
//            public void onClick() {
//
//            }
//        });
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        rvScan.setLayoutManager(linearLayoutManager);
//        rvScan.setAdapter(adapter);


    //}

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

                mPresenter.getListProduct(orderId, turn.getTurn());

                mPresenter.getListScanPallet(orderId, batch);
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

    @Override
    public void refreshLayout() {
        rvScan.requestLayout();
    }

    @Override
    public void initList(boolean init) {
        this.init = init;
    }


    @Override
    public void showDialogWarningResidual(ProductListModel productListModel, ProductModel
            productModel, String barcode) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.err_pack_not_enough))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.saveBarcodeResidual(productListModel, productModel, batch, barcode);

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

    @Override
    public void showWarningPrint() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.text_title_noti));
        builder.setMessage(getString(R.string.err_nunber_pack_not_enough));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.text_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        goToPrint();
                    }
                });
        builder.setNegativeButton(getString(R.string.text_no), new DialogInterface.OnClickListener() {
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
    public void goToPrint() {
        PrintPalletActivity.start(getActivity(), orderId, tvCodeSO.getText().toString(), tvCustomerName.getText().toString(),
                floorId, tvFloor.getText().toString(), batch);
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

        if (batch == 0) {
            showError(getString(R.string.err_batch_is_empty));
            return;
        }
        String codeSO = tvCodeSO.getText().toString().substring(0, 2) + tvCodeSO.getText().toString().substring(5);
        if (edtBarcode.getText().toString().trim().contains(codeSO)) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.dialog_default_title))
                    .setContentText(getString(R.string.text_save_barcode))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.checkBarcode(edtBarcode.getText().toString().trim(), orderId, batch);
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
        } else {
            showError(getString(R.string.text_barcode_no_exist));
            startMusicError();
            turnOnVibrator();
        }


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
//                                    tvBatch.setText(getString(R.string.text_choose_batch_scan));
//                                    batch = 0;
//                                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
//                                    orderId = 0;
//                                    tvFloor.setText(getString(R.string.text_choose_floor_print));
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
                tvBatch.setText(getString(R.string.text_choose_batch_scan));
                batch = 0;
                tvCodeSO.setText(getString(R.string.text_choose_code_so));
                orderId = 0;
                tvFloor.setText(getString(R.string.text_choose_floor_print));
                floorId = 0;
                tvCustomerName.setText("");
                mPresenter.getListSO(typeSO.getValue());
            }
        });
        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
//        }


    }

    @OnClick(R.id.bt_print)
    public void print() {
        if (floorId == 0) {
            showError(getString(R.string.err_floor_is_empty));
            return;
        }
        mPresenter.checkEnoughPack(orderId, floorId, batch);
    }

}
