package com.efortshub.zikr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efortshub.zikr.databinding.RowSearchItemListBinding;
import com.efortshub.zikr.models.DuaDetailsWithTitle;

import java.util.List;

public class SearchItemRvAdapter extends RecyclerView.Adapter<SearchItemRvAdapter.MyViewHolder> {


    private List<DuaDetailsWithTitle> duaDetailsWithTitles;

    public SearchItemRvAdapter(List<DuaDetailsWithTitle> duaDetailsWithTitles) {

        this.duaDetailsWithTitles = duaDetailsWithTitles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowSearchItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RowSearchItemListBinding binding = holder.binding;

        int lp = position-1;
        if(lp>=0 && lp<duaDetailsWithTitles.size()){

            DuaDetailsWithTitle dua = duaDetailsWithTitles.get(position - 1 );



            binding.tvTitle.setText(dua.getTitle());
            binding.tvArabic.setText(dua.getArabic());
            binding.tvBottom.setText(dua.getBottom());
            binding.tvReference.setText(dua.getReference());
            binding.tvTranslation.setText(dua.getTranslations());
            binding.tvTransliteration.setText(dua.getTransliteration());
            binding.tvTop.setText(dua.getTop());

//        if (holder.getAdapterPosition() == 0) {
//            binding.topGap.setVisibility(View.INVISIBLE);
//        } else if (holder.getAdapterPosition() == duaDetailsWithTitles.size() - 1) {
//            binding.bottomGap.setVisibility(View.INVISIBLE);
//        }

            binding.mcv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return duaDetailsWithTitles.size() + 2;
    }

    public void setDuas(List<DuaDetailsWithTitle> duaFilteredList) {
        duaDetailsWithTitles = duaFilteredList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RowSearchItemListBinding binding;

        public MyViewHolder(@NonNull RowSearchItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
