package com.efortshub.zikr.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.efortshub.zikr.R;
import com.efortshub.zikr.activities.DuaDetailsActivity;
import com.efortshub.zikr.adapter.AllItemRvAdapter;
import com.efortshub.zikr.adapter.HistoryItemRvAdapter;
import com.efortshub.zikr.databinding.FragmentFavoriteBinding;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;
import com.efortshub.zikr.models.DuaDetailsWithHistory;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.models.Favorite;
import com.efortshub.zikr.models.History;
import com.efortshub.zikr.utils.DbUtils;
import com.efortshub.zikr.utils.HbUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class FavoriteFragment extends Fragment {
    private static BottomNavAnimationListener listener;

    FragmentFavoriteBinding binding;
    boolean isFavoriteTab = true;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    public static FavoriteFragment newInstance(BottomNavAnimationListener listener) {
        FavoriteFragment.listener = listener;
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(requireActivity(), "resume with: "+ (isFavoriteTab?"Favorite":"History"), Toast.LENGTH_SHORT).show();
        toggleFavoriteHistory(isFavoriteTab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        listener.shouldShowBottomNavNow();

        toggleFavoriteHistory(true);


        binding.btnHistory.setOnClickListener(v -> {
            toggleFavoriteHistory(false);
        });
        binding.btnFavorite.setOnClickListener(v -> {
            toggleFavoriteHistory(true);
        });


        return binding.getRoot();
    }

    private void toggleFavoriteHistory(boolean isFav) {
        isFavoriteTab = isFav;
        if (isFav) {
            binding.btnFavorite.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_active_card, null));
            binding.btnHistory.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_card, null));
            loadFavorite();
        } else {
            binding.btnFavorite.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_card, null));
            binding.btnHistory.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_active_card, null));
            loadHistory();
        }
    }


    private void loadFavorite() {
        List<Favorite> favorites = DbUtils.getAllFavorite(requireContext());
        int[] array = new int[favorites.size()];
        for (int i = 0; i < favorites.size(); i++) {
            array[i] = favorites.get(i).getDuaId();
        }


        List<DuaDetailsWithTitle> dx = HbUtils.getDuaOfArray(requireContext(), array);
        List<DuaDetailsWithTitle> duaDetailsWithTitleList = new ArrayList<>();

        for (DuaDetailsWithTitle d : dx) {
            for (Favorite f : favorites) {
                if (f.getDuaId() == Integer.parseInt(d.getDua_global_id())) {
                    if (d.getDua_segment_id().equals(f.getSegmentId())) {
                        duaDetailsWithTitleList.add(d);
                    }
                }

            }
        }


        AllItemRvAdapter adapter = new AllItemRvAdapter(duaDetailsWithTitleList, details -> {
            Intent i = new Intent(requireActivity(), DuaDetailsActivity.class);
            i.putExtra("dua", details);
            i.putExtra("full", false);
            i.putExtra("list", array);

            startActivity(i);

        });
        binding.rvMain.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
        animAdapter.setDuration(500);
        animAdapter.setStartPosition(0);
        animAdapter.setInterpolator(new OvershootInterpolator(2f));
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
                    duaFilteredList.addAll(duaDetailsWithTitleList);
                } else {
                    for (DuaDetailsWithTitle dd : duaDetailsWithTitleList) {
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


        binding.rvMain.setVisibility(View.VISIBLE);




    }

    private void loadHistory() {
        List<History> hs = DbUtils.getAllHistory(requireContext());
        int[] array = new int[hs.size()];
        for (int i = 0; i < hs.size(); i++) {
            array[i] = hs.get(i).getDuaId();
        }


        List<DuaDetailsWithTitle> dx = HbUtils.getDuaOfArray(requireContext(), array);
        List<DuaDetailsWithHistory> duaDetailsWithTitleList = new ArrayList<>();

        for (DuaDetailsWithTitle d : dx) {
            for (History f : hs) {
                if (f.getDuaId() == Integer.parseInt(d.getDua_global_id())) {
                    if (d.getDua_segment_id().equals(f.getSegmentId())) {
                        duaDetailsWithTitleList.add(new DuaDetailsWithHistory(d.getDua_segment_id(), d.getTop(), d.getArabic_diacless(), d.getArabic(), d.getTransliteration(), d.getTranslations(), d.getBottom(), d.getReference(), d.getDua_global_id(), d.getTitle(), d.getSegmentIndex(), f.getMilliseconds()));
                    }
                }

            }
        }


        HistoryItemRvAdapter adapter = new HistoryItemRvAdapter(duaDetailsWithTitleList, details -> {
            Intent i = new Intent(requireActivity(), DuaDetailsActivity.class);
            i.putExtra("dua", details);
            i.putExtra("full", false);
            i.putExtra("list", array);

            startActivity(i);

        });
        binding.rvMain.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
        animAdapter.setDuration(500);
        animAdapter.setStartPosition(0);
        animAdapter.setInterpolator(new OvershootInterpolator(2f));
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
                List<DuaDetailsWithHistory> duaFilteredList = new ArrayList<>();

                if (s.toString().trim().isEmpty()) {
                    duaFilteredList.addAll(duaDetailsWithTitleList);
                } else {
                    for (DuaDetailsWithHistory dd : duaDetailsWithTitleList) {
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
        binding.rvMain.setVisibility(View.VISIBLE);


    }
}