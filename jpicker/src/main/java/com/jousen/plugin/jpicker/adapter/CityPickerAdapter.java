package com.jousen.plugin.jpicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jousen.plugin.jpicker.R;
import com.jousen.plugin.jpicker.model.CityData;

import java.util.ArrayList;
import java.util.List;

public class CityPickerAdapter extends RecyclerView.Adapter<CityPickerAdapter.VH> {
    private List<CityData> items;
    private OnItemClickListener onItemClickListener;

    public CityPickerAdapter() {
        this.items = new ArrayList<>();
    }

    public void setData(List<CityData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CityData data = items.get(position);
        holder.text.setText(data.t);
        holder.text.setOnClickListener(v -> onItemClickListener.itemClick(position, data));
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picker_items, parent, false);
        return new VH(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        public final TextView text;

        public VH(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.text);
        }
    }
}
