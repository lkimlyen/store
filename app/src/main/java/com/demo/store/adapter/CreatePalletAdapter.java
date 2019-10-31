package com.demo.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.CreatePalletModel;
import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.store.R;
import com.demo.store.app.CoreApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

public class CreatePalletAdapter extends RealmRecyclerViewAdapter<CreatePalletModel, CreatePalletAdapter.HistoryHolder> {

    private OnItemListener listener;

    public CreatePalletAdapter(OrderedRealmCollection<CreatePalletModel> realmResults, OnItemListener listener) {
        super(realmResults, true);
        this.listener = listener;

        setHasStableIds(true);
    }


    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_floor, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final CreatePalletModel obj = getItem(position);
        setDataToViews(holder, obj);

    }

    private void setDataToViews(HistoryHolder holder, CreatePalletModel item) {
        holder.tvFloor.setText(item.getFloorName() +" - " + item.getApartmentName());
        DetailProductAdapter adapter = new DetailProductAdapter(item.getProductList().sort("date", Sort.DESCENDING), new DetailProductAdapter.OnItemListener() {
            @Override
            public void onItemClick(DetailProductScanModel detail) {
                listener.onItemClick(item.getId(),detail.getId());
            }

            @Override
            public void onEditTextChange(DetailProductScanModel detail, int number) {
                listener.onEditTextChange(item.getId(), detail.getId(), number);
            }

            @Override
            public void errorListener(DetailProductScanModel item, int number, String message) {
                listener.errorListener(message);
            }
        });

        holder.rvData.setLayoutManager(new LinearLayoutManager(CoreApplication.getInstance()));
        holder.rvData.setAdapter(adapter);
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_floor)
        TextView tvFloor;
        @BindView(R.id.rv_data)
        RecyclerView rvData;

        private HistoryHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

    public interface OnItemListener {
        void onItemClick(long id,long detailId);

        void onEditTextChange(long id, long detailId, int number);

        void errorListener(String message);
    }
}