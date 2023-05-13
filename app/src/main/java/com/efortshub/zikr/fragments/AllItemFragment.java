package com.efortshub.zikr.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efortshub.zikr.R;
import com.efortshub.zikr.adapter.AllItemRvAdapter;
import com.efortshub.zikr.databinding.FragmentAllItemBinding;
import com.efortshub.zikr.models.Dua;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.utils.HbUtils;

import java.util.ArrayList;
import java.util.List;

public class AllItemFragment extends Fragment {
    FragmentAllItemBinding binding;
    public AllItemFragment() {
        // Required empty public constructor
    }

    public static AllItemFragment newInstance() {
        AllItemFragment fragment = new AllItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllItemBinding.inflate(inflater,container,false);

        List<DuaDetailsWithTitle> duaList = HbUtils.getAllDua(requireContext());
        AllItemRvAdapter adapter = new AllItemRvAdapter(duaList);
        binding.rvMain.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        binding.rvMain.setItemAnimator(new SimpleItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;
            }

            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(@NonNull RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        });
        binding.rvMain.setAdapter(adapter);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<DuaDetailsWithTitle> duaFilteredList = new ArrayList<>();

                if(s.toString().trim().isEmpty()){
                    duaFilteredList.addAll(duaList);
                }else{
                    for(DuaDetailsWithTitle dd : duaList){
                        if(dd.getTitle().trim().toLowerCase().contains(s.toString().trim().toLowerCase())){
                            duaFilteredList.add(dd);
                        }
                    }
                }
                adapter.setList(duaFilteredList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });








        return binding.getRoot();
    }
}