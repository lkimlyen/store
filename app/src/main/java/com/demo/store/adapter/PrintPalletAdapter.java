package com.demo.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.DetailProductScanModel;
import com.demo.store.R;

import java.util.List;

public class PrintPalletAdapter extends RecyclerView.Adapter<PrintPalletAdapter.MyViewHolder> {
    public PrintPalletAdapter(List<DetailProductScanModel> list) {
        this.list = list;
    }

    private List<DetailProductScanModel> list;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvNumber, tvBarcode, tvPack;

        public MyViewHolder(View v) {
            super(v);
            tvBarcode = v.findViewById(R.id.tv_barcode);
            tvNumber = v.findViewById(R.id.tv_number);
            tvPack = v.findViewById(R.id.tv_pack);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PrintPalletAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_print_pallet_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvBarcode.setText(list.get(position).getBarcode());
        holder.tvNumber.setText(list.get(position).getNumber()+"");
        holder.tvPack.setText(list.get(position).getPack());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }
}