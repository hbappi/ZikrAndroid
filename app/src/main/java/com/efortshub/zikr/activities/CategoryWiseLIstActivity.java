package com.efortshub.zikr.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.efortshub.zikr.R;
import com.efortshub.zikr.adapter.AllItemRvAdapter;
import com.efortshub.zikr.databinding.ActivityCategoryWiseListBinding;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.utils.HbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class CategoryWiseLIstActivity extends AppCompatActivity {
    ActivityCategoryWiseListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryWiseListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        int[] arr = getIntent().getIntArrayExtra("list");
        String title = getIntent().getStringExtra("title");

        binding.tvTitle.setText(title);
        binding.btnBack.setOnClickListener(v -> finish());

        Executors.newSingleThreadExecutor()
                .execute(() -> {
                    List<DuaDetailsWithTitle> duaList = HbUtils.getDuaOfArray(getApplicationContext(), arr);
                    AllItemRvAdapter adapter = new AllItemRvAdapter(duaList, details -> {
                        Log.d("hbhb", "onCreateView: dua clidked " + details.getTitle());
                        Intent i = new Intent(CategoryWiseLIstActivity.this, DuaDetailsActivity.class);
                        i.putExtra("dua", details);
                        i.putExtra("full", false);
                        i.putExtra("list", arr);

                        startActivity(i);

                    });


                    new Handler(Looper.getMainLooper()).post(() -> {
                        binding.rvMain.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
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
                        binding.rvMain.setVisibility(View.VISIBLE);
                    });
                });
    }
}