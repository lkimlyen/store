package com.demo.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.ExportModel;
import com.demo.store.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ExportAdapter extends RealmRecyclerViewAdapter<ExportModel, ExportAdapter.HistoryHolder> {


    public ExportAdapter(OrderedRealmCollection<ExportModel> realmResults) {
        super(realmResults, true);

        setHasStableIds(true);
    }


    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_export, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final ExportModel obj = getItem(position);
        setDataToViews(holder, obj);

    }

    private void setDataToViews(HistoryHolder holder, ExportModel item) {

        holder.tvCode.setText(item.getCode());
        holder.tvDate.setText(item.getDateExport());
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_code)
        TextView tvCode;
        @BindView(R.id.tv_date)
        TextView tvDate;


        private HistoryHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

}