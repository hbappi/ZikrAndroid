package com.efortshub.zikr.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.efortshub.zikr.R;
import com.efortshub.zikr.databinding.ActivityDuaDetailsBinding;
import com.efortshub.zikr.databinding.DialogGoToBinding;
import com.efortshub.zikr.models.Dua;
import com.efortshub.zikr.models.DuaDetails;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.utils.HbUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;

public class DuaDetailsActivity extends AppCompatActivity {
    ActivityDuaDetailsBinding binding;
    boolean isFull = true;
    List<DuaDetailsWithTitle> duaDetailsWithTitleList = new ArrayList<>();
    DuaDetailsWithTitle dua;
    int max = 328;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDuaDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (HbUtils.getLanguageCode(getApplicationContext()).equals("bn")) {
            max = 422;
        }


        isFull = getIntent().getBooleanExtra("full", true);
        if (isFull) {
            dua = (DuaDetailsWithTitle) getIntent().getSerializableExtra("dua");
            setDua(dua);
        } else {

            int[] arr = getIntent().getIntArrayExtra("dua");

            Executors.newSingleThreadExecutor().execute(() -> {

                int index = 0;
                for (int i : arr) {
                    Dua d = HbUtils.getDuaOfIndex(getApplicationContext(), i);

                    if (duaDetailsWithTitleList == null)
                        duaDetailsWithTitleList = new ArrayList<>();
                    if(d!=null){
                        if(d.getDetails()!=null){
                            for (DuaDetails dd : d.getDetails()) {
                                duaDetailsWithTitleList.add(
                                        new DuaDetailsWithTitle(
                                                dd.getDua_segment_id(),
                                                dd.getTop(),
                                                dd.getArabic_diacless(),
                                                dd.getArabic(),
                                                dd.getTransliteration(),
                                                dd.getTranslations(),
                                                dd.getBottom(),
                                                dd.getReference(),
                                                dd.getDua_global_id(),
                                                d.getPageTitle(),
                                                index++));

                            }
                        }
                    }

                }

                runOnUiThread(() -> {
                    setDua(duaDetailsWithTitleList.get(0));
                    max = duaDetailsWithTitleList.size();
                    initFunctions();

                });

            });
        }

        if (dua != null) {
            initFunctions();
        }


        binding.btnBack.setOnClickListener(v -> {
            finish();
        });


    }

    private void initFunctions() {


        binding.btnNext.setOnClickListener(v -> {
            if (isFull) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    if (duaDetailsWithTitleList.isEmpty()) {
                        duaDetailsWithTitleList = HbUtils.getAllDua(getApplicationContext());
                    }


                    if (dua.getSegmentIndex() < max - 1) {
                        new Handler(Looper.getMainLooper()).post(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() + 1)));
                    }
                });

            } else {
                if (dua.getSegmentIndex() < max - 1) {
                    runOnUiThread(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() + 1)));
                }
            }

        });
        binding.btnPrevious.setOnClickListener(v -> {
            if (isFull) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    if (duaDetailsWithTitleList.isEmpty()) {
                        duaDetailsWithTitleList = HbUtils.getAllDua(getApplicationContext());
                        Collections.sort(duaDetailsWithTitleList, new Comparator<DuaDetailsWithTitle>() {
                            @Override
                            public int compare(DuaDetailsWithTitle o1, DuaDetailsWithTitle o2) {
                                return Integer.compare(o1.getSegmentIndex(), o2.getSegmentIndex());
                            }
                        });
                    }

                    if (dua.getSegmentIndex() > 0) {
                        new Handler(Looper.getMainLooper()).post(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() - 1)));
                    }
                });


            } else {
                if (dua.getSegmentIndex() > 0) {
                    runOnUiThread(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() - 1)));
                }
            }

        });

        binding.tvCurrentSegment.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(DuaDetailsActivity.this);

            DialogGoToBinding db = DialogGoToBinding.inflate(getLayoutInflater(), null, false);

            builder.setCancelable(true);
            builder.setView(db.getRoot());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            db.etGoto.setHint("1 - " + duaDetailsWithTitleList.size());

            db.etGoto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (Integer.parseInt(s.toString()) < 1) {
                        db.etGoto.setText("1");
                    } else if (Integer.parseInt(s.toString()) > duaDetailsWithTitleList.size()) {
                        db.etGoto.setText((duaDetailsWithTitleList.size()) + "");
                    }

                }
            });

            db.btnGo.setOnClickListener(v1 -> {
                if (!db.etGoto.getText().toString().trim().isEmpty()) {
                    int i = Integer.parseInt(db.etGoto.getText().toString());
                    if (alertDialog.isShowing()) {
                        alertDialog.cancel();
                    }
                    setDua(duaDetailsWithTitleList.get(i - 1));
                } else {
                    db.etGoto.setError("Please enter a number");
                }
            });

        });

    }

    private void setDua(DuaDetailsWithTitle dua) {
        Log.d("hbhb", "setDua: setting dua of : " + dua.getSegmentIndex());

        if (dua != null) {
            this.dua = dua;
            binding.tvTitle.setText(dua.getTitle());
            binding.tvArabic.setText(dua.getArabic());
            binding.tvBottom.setText(dua.getBottom());
            binding.tvReference.setText(dua.getReference());
            binding.tvTranslation.setText(dua.getTranslations());
            binding.tvTransliteration.setText(dua.getTransliteration());
            String dsi = "(" + dua.getDua_global_id() + (dua.getDua_segment_id().length() > 0 ? "." : "") + dua.getDua_segment_id() + ")\n " + (dua.getSegmentIndex() + 1) + (duaDetailsWithTitleList.size() > 0 ? "/" + duaDetailsWithTitleList.size() : "") + " ";
            binding.tvCurrentSegment.setText(dsi);

        }
    }
}