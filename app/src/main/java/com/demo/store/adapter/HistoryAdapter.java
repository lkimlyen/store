package com.demo.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.CodePalletEntity;
import com.demo.store.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    public HistoryAdapter(List<CodePalletEntity> list, OnPrintListener listener) {
        this.list = list;
        this.listener = listener;
    }

    private List<CodePalletEntity> list;

    private OnPrintListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvCode, tvDate, tvStatus, tvNumber;

        public MyViewHolder(View v) {
            super(v);

            tvCode = v.findViewById(R.id.tv_code);
            tvDate = v.findViewById(R.id.tv_date);
            tvStatus = v.findViewById(R.id.tv_status);
            tvNumber = v.findViewById(R.id.tv_number);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvCode.setText(list.get(position).getCode());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvStatus.setText(list.get(position).getDescription());
        holder.tvNumber.setText(list.get(position).getCountNum());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPrint(list.get(position).getCode());
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnPrintListener {
        void onPrint(String code);
    }
}