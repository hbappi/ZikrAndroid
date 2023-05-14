package com.efortshub.zikr.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.efortshub.zikr.R;
import com.efortshub.zikr.adapter.SearchItemRvAdapter;
import com.efortshub.zikr.databinding.FragmentSearchBinding;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.utils.HbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class SearchFragment extends Fragment {
    private static BottomNavAnimationListener listener;
    String searchQuery = "";
    FragmentSearchBinding binding;
    List<DuaDetailsWithTitle> duaDetailsWithTitleList = new ArrayList<>();
    List<DuaDetailsWithTitle> duaFilteredList = new ArrayList<>();
    SearchItemRvAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(BottomNavAnimationListener listener) {
        SearchFragment.listener = listener;
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        Executors.newSingleThreadExecutor()
                .execute(() -> {

                    duaDetailsWithTitleList = HbUtils.getAllDua(requireContext());
                    adapter = new SearchItemRvAdapter(duaFilteredList);

                    AnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
                    animationAdapter.setDuration(1000);
                    animationAdapter.setFirstOnly(false);
                    animationAdapter.setInterpolator(new OvershootInterpolator(1f));
                    new Handler(Looper.getMainLooper()).post(() -> {

                        binding.rvMain.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                        binding.rvMain.setAdapter(animationAdapter);

                        listener.shouldShowBottomNavNow();
                    });

                });


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearchQuery(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return binding.getRoot();
    }

    private void updateSearchQuery(CharSequence s) {
        duaFilteredList = new ArrayList<>();

        if (!s.toString().trim().isEmpty()) {
            String sq = s.toString().trim().toLowerCase();
            for (DuaDetailsWithTitle dd : duaDetailsWithTitleList) {
                if (dd.getTitle().trim().toLowerCase().contains(sq) ||
                        dd.getReference().trim().toLowerCase().contains(sq) ||
                        dd.getArabic().trim().contains(sq) ||
                        dd.getTop().trim().toLowerCase().contains(sq) ||
                        dd.getBottom().trim().toLowerCase().contains(sq) ||
                        dd.getTranslations().trim().toLowerCase().contains(sq) ||
                        dd.getTransliteration().trim().toLowerCase().contains(sq)
                ) {
                    duaFilteredList.add(dd);
                }
            }
        }

        if (adapter!=null && !duaFilteredList.isEmpty()) {
            binding.lvNoData.setVisibility(View.GONE);
            binding.rvMain.setVisibility(View.VISIBLE);
            adapter.setDuas(duaFilteredList);
            adapter.notifyDataSetChanged();
        }else{
            binding.rvMain.setVisibility(View.GONE);
            binding.lvNoData.setVisibility(View.VISIBLE);
        }


    }
}