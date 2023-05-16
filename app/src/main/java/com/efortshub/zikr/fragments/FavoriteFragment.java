package com.efortshub.zikr.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.efortshub.zikr.R;
import com.efortshub.zikr.activities.DuaDetailsActivity;
import com.efortshub.zikr.adapter.AllItemRvAdapter;
import com.efortshub.zikr.databinding.FragmentFavoriteBinding;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;
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

    private void loadHistory() {
//
//        List<History> histories =  DbUtils.getAllHistory(requireContext());
//
//
//        AllItemRvAdapter adapter = new AllItemRvAdapter(duaList, details -> {
//            Log.d("hbhb", "onCreateView: dua clidked "+ details.getTitle());
//            Intent i = new Intent(requireActivity(), DuaDetailsActivity.class);
//            i.putExtra("dua", details);
//            i.putExtra("full", true);
//
//            startActivity(i);
//
//        });
//

//
//
//        binding.rvMain.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
//        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
//        animAdapter.setDuration(500);
//        animAdapter.setStartPosition(0);
//        animAdapter.setInterpolator(new OvershootInterpolator(2f));
//        animAdapter.setStartPosition(0);
//        animAdapter.setFirstOnly(false);
//        animAdapter.setHasStableIds(false);
//        binding.rvMain.setAdapter(animAdapter);
    }

    private void loadFavorite() {
        List<Favorite> favorites =  DbUtils.getAllFavorite(requireContext());
        int[] array = new int[favorites.size()];
        for(int i =0; i<favorites.size(); i++){
            array[i] = favorites.get(i).getDuaId();
        }




        List<DuaDetailsWithTitle> duaDetailsWithTitleList = HbUtils.getDuaOfArray(requireContext(), array);

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

        binding.rvMain.setVisibility(View.VISIBLE);


    }
}