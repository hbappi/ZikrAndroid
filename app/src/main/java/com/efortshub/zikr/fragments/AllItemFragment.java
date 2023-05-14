package com.efortshub.zikr.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.efortshub.zikr.activities.DuaDetailsActivity;
import com.efortshub.zikr.adapter.AllItemRvAdapter;
import com.efortshub.zikr.databinding.FragmentAllItemBinding;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;
import com.efortshub.zikr.interfaces.ItemClickListener;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.utils.HbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;

public class AllItemFragment extends Fragment {
    private static BottomNavAnimationListener listener;
    FragmentAllItemBinding binding;

    public AllItemFragment() {
        // Required empty public constructor
    }

    public static AllItemFragment newInstance(BottomNavAnimationListener listener) {
        AllItemFragment.listener = listener;
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
        binding = FragmentAllItemBinding.inflate(inflater, container, false);

        Executors.newSingleThreadExecutor()
                .execute(() -> {
                    List<DuaDetailsWithTitle> duaList = HbUtils.getAllDua(requireContext());
                    AllItemRvAdapter adapter = new AllItemRvAdapter(duaList, details -> {
                        Log.d("hbhb", "onCreateView: dua clidked "+ details.getTitle());
                        Intent i = new Intent(requireActivity(), DuaDetailsActivity.class);
                        i.putExtra("dua", details);
                        i.putExtra("full", true);

                        startActivity(i);

                    });


                    new Handler(Looper.getMainLooper()).post(() -> {
                        binding.rvMain.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                        SlideInLeftAnimationAdapter animAdapter = new SlideInLeftAnimationAdapter(adapter);
                        animAdapter.setDuration(500);
                        animAdapter.setStartPosition(0);
                        animAdapter.setInterpolator(new OvershootInterpolator(1f));
                        animAdapter.setStartPosition(0);
                        animAdapter.setFirstOnly(false);
                        animAdapter.setHasStableIds(false);
                        binding.rvMain.setAdapter(animAdapter);
                        binding.etSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                List<DuaDetailsWithTitle> duaFilteredList = new ArrayList<>();

                                if (s.toString().trim().isEmpty()) {
                                    duaFilteredList.addAll(duaList);
                                } else {
                                    for (DuaDetailsWithTitle dd : duaList) {
                                        if (dd.getTitle().trim().toLowerCase().contains(s.toString().trim().toLowerCase())) {
                                            duaFilteredList.add(dd);
                                        }
                                    }
                                }
                                adapter.setList(duaFilteredList);
                                animAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        listener.shouldShowBottomNavNow();
                        binding.rvMain.setVisibility(View.VISIBLE);
                    });
                });

        return binding.getRoot();
    }
}