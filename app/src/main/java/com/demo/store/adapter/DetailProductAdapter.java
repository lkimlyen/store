package com.demo.store.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.architect.data.model.offline.LogScanProduct;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class DetailProductAdapter extends RealmRecyclerViewAdapter<DetailProductScanModel, DetailProductAdapter.HistoryHolder> {

    private OnItemListener listener;

    public DetailProductAdapter(OrderedRealmCollection<DetailProductScanModel> realmResults, OnItemListener listener) {
        super(realmResults, true);
        this.listener = listener;

        setHasStableIds(true);
    }


    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_create_pallet, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final DetailProductScanModel obj = getItem(position);
        setDataToViews(holder, obj);

    }

    private void setDataToViews(HistoryHolder holder, DetailProductScanModel item) {

        LogScanProduct log = item.getLogScanProduct();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int numberInput = Integer.parseInt(s.toString());
                    if (numberInput <= 0) {
                        holder.edtNumberScan.setText((int) item.getNumber() + "");
                        listener.errorListener(item, numberInput, CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                        return;

                    }
                    if (numberInput > item.getMaxNumber()) {
                        holder.edtNumberScan.setText((int) item.getNumber() + "");
                        listener.errorListener(item, numberInput, CoreApplication.getInstance().getString(R.string.text_number_input_bigger_number_request));
                        return;
                    }
                    if (numberInput == item.getNumber()) {
                        return;
                    }
                    listener.onEditTextChange(item, numberInput);

                } catch (Exception e) {

                }
            }
        };

        holder.tvBarcode.setText(item.getBarcode());
        holder.txtNameDetail.setText(item.getProductName());
        holder.txtQuantityProduct.setText((int) log.getNumberTotal() + "");
        holder.txtQuantityRest.setText((int) log.getNumberRest() + "");
        holder.txtQuantityScan.setText((int) log.getNumberScanned() + "");
        holder.edtNumberScan.setText(String.valueOf((int) item.getNumber()));
        holder.edtNumberScan.setSelection(holder.edtNumberScan.getText().length());

        //kiểm tra item này thuộc loại quét nào nếu là loại quét theo group thì hiển thị thêm nhóm và show tất cả chi tiết nằm trong group này

        holder.edtNumberScan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    holder.edtNumberScan.addTextChangedListener(textWatcher);
                } else {

                    holder.edtNumberScan.removeTextChangedListener(textWatcher);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

        if (log.getNumberRest() > 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
        } else if (log.getNumberRest() == 0) {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorGreen));
            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.white));

        } else {
            holder.llMain.setBackgroundColor(CoreApplication.getInstance().getResources().getColor(R.color.colorYellow));
            holder.edtNumberScan.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
            holder.txtQuantityRest.setTextColor(CoreApplication.getInstance().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_barcode)
        TextView tvBarcode;
        @BindView(R.id.tv_name_product)
        TextView txtNameDetail;

        @BindView(R.id.iv_delete)
        ImageView imgDelete;

        @BindView(R.id.tv_quantity_total)
        TextView txtQuantityProduct;

        @BindView(R.id.tv_quantity_rest)
        TextView txtQuantityRest;

        @BindView(R.id.tv_quantity_scan)
        TextView txtQuantityScan;

        @BindView(R.id.et_number)
        EditText edtNumberScan;
        @BindView(R.id.layout_main)
        RelativeLayout llMain;

        private HistoryHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

    public interface OnItemListener {
        void onItemClick(DetailProductScanModel item);
        void onEditTextChange(DetailProductScanModel item, int number);
        void errorListener(DetailProductScanModel item, int number, String message);
    }


}