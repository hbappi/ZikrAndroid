package com.efortshub.zikr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efortshub.zikr.databinding.RowAllItemListBinding;
import com.efortshub.zikr.models.DuaDetailsWithTitle;

import java.util.List;

public class AllItemRvAdapter extends RecyclerView.Adapter<AllItemRvAdapter.MyHolder> {
    private List<DuaDetailsWithTitle> duaList;

    public AllItemRvAdapter(List<DuaDetailsWithTitle> duaList) {
        this.duaList = duaList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowAllItemListBinding binding = RowAllItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        DuaDetailsWithTitle duaDetails = duaList.get(position);
        RowAllItemListBinding binding = holder.binding;


        String count = duaDetails.getDua_global_id() + (duaDetails.getDua_segment_id().trim().isEmpty() ? "" : "." + duaDetails.getDua_segment_id());
        binding.tvCount.setText(count);
        binding.tvTitle.setText(duaDetails.getTitle());


    }

    @Override
    public int getItemCount() {
        return duaList.size();
    }

    public void setList(List<DuaDetailsWithTitle> duaFilteredList) {
        duaList = duaFilteredList;
    }


    class MyHolder extends RecyclerView.ViewHolder {
        RowAllItemListBinding binding;

        public MyHolder(@NonNull RowAllItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
