package com.efortshub.zikr.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.efortshub.zikr.R;
import com.efortshub.zikr.databinding.ActivityDuaDetailsBinding;
import com.efortshub.zikr.databinding.DialogGoToBinding;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.utils.DbUtils;
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
    final int DIRECTION_LEFT = 1, DIRECTION_RIGHT = 2, DIRECTION_UP = 0;
    private static final String TAG = "hbhbxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDuaDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int height = binding.getRoot().getHeight();
        binding.lx.setMinimumHeight(height);

        if (HbUtils.getLanguageCode(getApplicationContext()).equals("bn")) {
            max = 422;
        }


        binding.tvTitle.setText("");
        binding.tvArabic.setText("");
        binding.tvBottom.setText("");
        binding.tvReference.setText("");
        binding.tvTranslation.setText("");
        binding.tvTransliteration.setText("");
        binding.tvCurrentSegment.setText("");


        isFull = getIntent().getBooleanExtra("full", true);
        if (isFull) {
            dua = (DuaDetailsWithTitle) getIntent().getSerializableExtra("dua");
            setDua(dua, DIRECTION_UP);
        } else {

            dua = (DuaDetailsWithTitle) getIntent().getSerializableExtra("dua");
            int[] arr = getIntent().getIntArrayExtra("list");

            Executors.newSingleThreadExecutor().execute(() -> {

                duaDetailsWithTitleList = HbUtils.getDuaOfArray(getApplicationContext(), arr);

                runOnUiThread(() -> {
                    setDua(dua != null ? dua : duaDetailsWithTitleList.get(0), DIRECTION_UP);
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
                        new Handler(Looper.getMainLooper()).post(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() + 1), DIRECTION_LEFT));
                    }
                });

            } else {
                if (dua.getSegmentIndex() < max - 1) {
                    runOnUiThread(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() + 1), DIRECTION_LEFT));
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
                        new Handler(Looper.getMainLooper()).post(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() - 1), DIRECTION_RIGHT));
                    }
                });


            } else {
                if (dua.getSegmentIndex() > 0) {
                    runOnUiThread(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() - 1), DIRECTION_RIGHT));
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
                    setDua(duaDetailsWithTitleList.get(i - 1), DIRECTION_UP);
                } else {
                    db.etGoto.setError("Please enter a number");
                }
            });

        });

    }

    private void setDua(DuaDetailsWithTitle dua, int direction) {
        Log.d("hbhb", "setDua: setting dua of : " + dua.getSegmentIndex());

        binding.lx.setMinimumHeight(binding.getRoot().getHeight());

        if (dua != null) {
            this.dua = dua;

            binding.tvTitle.setText(HbUtils.getHtmlText(dua.getTitle()));

            String dsi = "(" + dua.getDua_global_id() + (dua.getDua_segment_id().length() > 0 ? "." : "") + dua.getDua_segment_id() + ")<br/> " + (dua.getSegmentIndex() + 1) + (duaDetailsWithTitleList.size() > 0 ? "/" + duaDetailsWithTitleList.size() : "") + " ";

            checkFavorite(dua);

            switch (direction) {
                case DIRECTION_UP:
                    animUpSetText(binding.tvTop, HbUtils.getHtmlText(dua.getTop()), 0);
                    animUpSetText(binding.tvArabic, HbUtils.getHtmlText(dua.getArabic()), 100);
                    animUpSetText(binding.tvTranslation, HbUtils.getHtmlText(dua.getTranslations()), 200);
                    animUpSetText(binding.tvTransliteration, HbUtils.getHtmlText(dua.getTransliteration()), 300);
                    animUpSetText(binding.tvBottom, HbUtils.getHtmlText(dua.getBottom()), 400);
                    animUpSetText(binding.tvReference, HbUtils.getHtmlText(dua.getReference()), 500);
                    animUpSetText(binding.tvCurrentSegment, HbUtils.getHtmlText(dsi), 600);
                    break;
                case DIRECTION_LEFT:
                    animLeftSetText(binding.tvTop, HbUtils.getHtmlText(dua.getTop()), 0);
                    animLeftSetText(binding.tvArabic, HbUtils.getHtmlText(dua.getArabic()), 60);
                    animLeftSetText(binding.tvTranslation, HbUtils.getHtmlText(dua.getTranslations()), 120);
                    animLeftSetText(binding.tvTransliteration, HbUtils.getHtmlText(dua.getTransliteration()), 180);
                    animLeftSetText(binding.tvBottom, HbUtils.getHtmlText(dua.getBottom()), 240);
                    animLeftSetText(binding.tvReference, HbUtils.getHtmlText(dua.getReference()), 300);
                    animLeftSetText(binding.tvCurrentSegment, HbUtils.getHtmlText(dsi), 360);
                    break;
                case DIRECTION_RIGHT:
                    animRightSetText(binding.tvTop, HbUtils.getHtmlText(dua.getTop()), 0);
                    animRightSetText(binding.tvArabic, HbUtils.getHtmlText(dua.getArabic()), 60);
                    animRightSetText(binding.tvTranslation, HbUtils.getHtmlText(dua.getTranslations()), 120);
                    animRightSetText(binding.tvTransliteration, HbUtils.getHtmlText(dua.getTransliteration()), 180);
                    animRightSetText(binding.tvBottom, HbUtils.getHtmlText(dua.getBottom()), 240);
                    animRightSetText(binding.tvReference, HbUtils.getHtmlText(dua.getReference()), 300);
                    animRightSetText(binding.tvCurrentSegment, HbUtils.getHtmlText(dsi), 360);
                    break;
                default:
            }


            DbUtils.addToHistory(getApplicationContext(), Integer.parseInt(dua.getDua_global_id()), dua.getDua_segment_id());
            DbUtils.addToFavorite(getApplicationContext(), Integer.parseInt(dua.getDua_global_id()), dua.getDua_segment_id());

        }
    }

    private void checkFavorite(DuaDetailsWithTitle dua) {

        Animation ta = new TranslateAnimation(0, 0, Math.negateExact(View.MeasureSpec.getSize(1000)), 0);
        ta.setDuration(600);
        ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

        binding.ivFavorite.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.ivFavorite.setTranslationY(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.lottieView.playAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void animUpSetText(TextView tv, Spanned text, int delay) {
        int height = binding.getRoot().getHeight();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Animation ta = new TranslateAnimation(0, 0, 0, Math.negateExact(View.MeasureSpec.getSize(height)));
            ta.setDuration(300);
            ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

            tv.startAnimation(ta);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tv.setText("");

                    Animation tx = new TranslateAnimation(0, 0, View.MeasureSpec.getSize(height), 0);
                    tx.setDuration(600);
                    tx.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

                    tv.startAnimation(tx);
                    tx.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            tv.setText(text);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }, delay);

    }
    private void animLeftSetText(TextView tv, Spanned text, int delay) {
        int width = binding.getRoot().getWidth();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Animation ta = new TranslateAnimation(0, Math.negateExact(View.MeasureSpec.getSize(width)), 0, 0);
            ta.setDuration(300);
            ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

            tv.startAnimation(ta);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tv.setText("");

                    Animation tx = new TranslateAnimation(View.MeasureSpec.getSize(width), 0, 0, 0);
                    tx.setDuration(600);
                    tx.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

                    tv.startAnimation(tx);
                    tx.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            tv.setText(text);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }, delay);

    }
    private void animRightSetText(TextView tv, Spanned text, int delay) {
        int width = binding.getRoot().getWidth();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Animation ta = new TranslateAnimation(0,View.MeasureSpec.getSize(width), 0, 0);
            ta.setDuration(300);
            ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

            tv.startAnimation(ta);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tv.setText("");

                    Animation tx = new TranslateAnimation(Math.negateExact( View.MeasureSpec.getSize(width)), 0,0, 0);
                    tx.setDuration(600);
                    tx.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

                    tv.startAnimation(tx);
                    tx.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            tv.setText(text);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }, delay);

    }

}