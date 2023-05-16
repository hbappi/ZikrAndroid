package com.efortshub.zikr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efortshub.zikr.databinding.RowHistoryItemListBinding;
import com.efortshub.zikr.interfaces.ItemHistoryClickListener;
import com.efortshub.zikr.models.DuaDetailsWithHistory;
import com.efortshub.zikr.models.DuaDetailsWithTitle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryItemRvAdapter extends RecyclerView.Adapter<HistoryItemRvAdapter.MyHolder> {
    private List<DuaDetailsWithHistory> duaList;
    private ItemHistoryClickListener itemClickListener;

    public HistoryItemRvAdapter(List<DuaDetailsWithHistory> duaList, ItemHistoryClickListener itemClickListener) {
        this.duaList = duaList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowHistoryItemListBinding binding = RowHistoryItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        DuaDetailsWithHistory duaDetails = duaList.get(position);
        RowHistoryItemListBinding binding = holder.binding;


        String count = duaDetails.getDua_global_id() + (duaDetails.getDua_segment_id().trim().isEmpty() ? "" : "." + duaDetails.getDua_segment_id());
        binding.tvCount.setText(count);
        binding.tvTitle.setText(duaDetails.getTitle());
        binding.getRoot().setOnClickListener(v -> itemClickListener.onClicked(duaDetails));
        String df = " hh:mm:ss a - dd/MM/yyyy";
        String dateText = "Last read:- " + new SimpleDateFormat(df, Locale.getDefault()).format(new Date(duaDetails.getTimestamp()));
        binding.tvTime.setText(dateText);

    }

    @Override
    public int getItemCount() {
        return duaList.size();
    }

    public void setList(List<DuaDetailsWithHistory> duaFilteredList) {
        duaList = duaFilteredList;
    }


    class MyHolder extends RecyclerView.ViewHolder {
        RowHistoryItemListBinding binding;

        public MyHolder(@NonNull RowHistoryItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
