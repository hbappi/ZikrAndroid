package com.efortshub.zikr.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FrameMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.efortshub.zikr.R;
import com.efortshub.zikr.databinding.ActivityDuaDetailsBinding;
import com.efortshub.zikr.databinding.DialogGoToBinding;
import com.efortshub.zikr.databinding.DialogSegmentFavoriteBinding;
import com.efortshub.zikr.models.DuaDetailsWithHistory;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.utils.DbUtils;
import com.efortshub.zikr.utils.HbUtils;

import java.sql.DriverManager;
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
    private GestureDetector gestureDetector;

    private float totalSwipeDistance = 0;
    private int screenWidth = 0;
    private static final String TAG = "hbhbxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDuaDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        screenWidth = binding.mcv.getMeasuredWidth();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        binding.lx.setMinimumHeight(height);

        if (HbUtils.getLanguageCode(getApplicationContext()).equals("bn")) {
            max = 422;
        }

        isFull = getIntent().getBooleanExtra("full", true);
        if (isFull) {
            dua = (DuaDetailsWithTitle) getIntent().getSerializableExtra("dua");
            setDua(dua, DIRECTION_UP, false);
        } else {

            try {

                Object o = getIntent().getSerializableExtra("dua");
                if (o instanceof DuaDetailsWithTitle) {
                    dua = (DuaDetailsWithTitle) o;
                } else if (o instanceof DuaDetailsWithHistory) {
                    DuaDetailsWithHistory ddh = (DuaDetailsWithHistory) o;
                    dua = new DuaDetailsWithTitle(ddh.getDua_segment_id(),
                            ddh.getTop(),
                            ddh.getArabic_diacless(),
                            ddh.getArabic(),
                            ddh.getTransliteration(),
                            ddh.getTranslations(),
                            ddh.getBottom(),
                            ddh.getReference(),
                            ddh.getDua_global_id(),
                            ddh.getTitle(),
                            ddh.getSegmentIndex());

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            int[] arr = getIntent().getIntArrayExtra("list");

            Executors.newSingleThreadExecutor().execute(() -> {

                duaDetailsWithTitleList = HbUtils.getDuaOfArray(getApplicationContext(), arr);

                runOnUiThread(() -> {
                    setDua(dua != null ? dua : duaDetailsWithTitleList.get(0), DIRECTION_UP, false);
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

    private void loadPreviousDua(boolean forceNoDelay) {

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
                    runOnUiThread(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() - 1), DIRECTION_RIGHT, forceNoDelay));
                } else {
                    runOnUiThread(this::backSwipeToZero);
                }
            });


        } else {
            if (dua.getSegmentIndex() > 0) {
                setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() - 1), DIRECTION_RIGHT, forceNoDelay);
            } else
                runOnUiThread(this::backSwipeToZero);
        }

    }

    private void loadNextDua(boolean forceNoDelay) {

        if (isFull) {
            Executors.newSingleThreadExecutor().execute(() -> {
                if (duaDetailsWithTitleList.isEmpty()) {
                    duaDetailsWithTitleList = HbUtils.getAllDua(getApplicationContext());
                }


                if (dua.getSegmentIndex() < duaDetailsWithTitleList.size()-1) {
                    runOnUiThread(() -> setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() + 1), DIRECTION_LEFT, forceNoDelay));
                } else
                    runOnUiThread(this::backSwipeToZero);
            });

        } else {
            if (dua.getSegmentIndex() < duaDetailsWithTitleList.size()-1) {
                setDua(duaDetailsWithTitleList.get(dua.getSegmentIndex() + 1), DIRECTION_LEFT, forceNoDelay);
            } else
                runOnUiThread(this::backSwipeToZero);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initFunctions() {


        binding.btnNext.setOnClickListener(v -> {
            loadNextDua(false);
        });
        binding.btnPrevious.setOnClickListener(v -> {
            loadPreviousDua(false);
        });

        binding.tvCurrentSegment.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(DuaDetailsActivity.this);

            DialogGoToBinding db = DialogGoToBinding.inflate(getLayoutInflater(), null, false);

//            HbUtils.blurViews(DuaDetailsActivity.this, 5f, db.blurMain);

            builder.setCancelable(true);
            builder.setView(db.getRoot());

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_transparent, null));

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
                    setDua(duaDetailsWithTitleList.get(i - 1), DIRECTION_UP, false);
                } else {
                    db.etGoto.setError("Please enter a number");
                }
            });

        });


        gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {

//                Log.d(TAG, "onScroll: scrolling: "+distanceX+" total: "+totalSwipeDistance);
                totalSwipeDistance += distanceX;

                float movementSensitivity = 1.5f;

                // Calculate the amount to move the view based on the total swipe distance
                float movementAmount = totalSwipeDistance * movementSensitivity;

                // Update the position of the view
//                binding.mcv.setTranslationX(-movementAmount);


                animSwipe(binding.tvTop, 0, -movementAmount);
                animSwipe(binding.tvArabic, 100, -movementAmount);
                animSwipe(binding.tvTranslation, 200, -movementAmount);
                animSwipe(binding.tvTransliteration, 300, -movementAmount);
                animSwipe(binding.tvBottom, 400, -movementAmount);
                animSwipe(binding.tvReference, 500, -movementAmount);
                animSwipe(binding.tvCurrentSegment, 600, -movementAmount);


                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                // Calculate the difference in coordinates
                float deltaX = e2.getX() - e1.getX();
                float deltaY = e2.getY() - e1.getY();

                // Check if the swipe is horizontal and long enough
                if (Math.abs(deltaX) > Math.abs(deltaY) &&
                        Math.abs(deltaX) > 50 &&
                        Math.abs(velocityX) > 20) {

//                    Log.d(TAG, "onFling: velocityx"+velocityX);

//                    if (deltaX > 0) {
//                        // Swipe from left to right (perform your task here)
//                        Log.d(TAG, "onFling: left to right");
////                        performTask();
//                    } else {
//                        // Swipe from right to left (perform your task here)
//                        Log.d(TAG, "onFling: right to left");
////                        performAnotherTask();
//                    }
                    return true;
                }

                return false;
            }

        });

        binding.mcv.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "onTouch: action up event: " + binding.tvArabic.getTranslationX() + " " + screenWidth);

                if (screenWidth * 0.3 < Math.abs(binding.tvArabic.getTranslationX())) {
                    totalSwipeDistance = 0;

                    if (binding.tvArabic.getTranslationX() > 0) {
                        //next dua
                        loadPreviousDua(true);

                    } else {
                        //prev dua
                        loadNextDua(true);

                    }

                } else {

                    backSwipeToZero();
                }
            }
            return gestureDetector.onTouchEvent(event);
        });


    }

    private void backSwipeToZero() {

        animSwipe(binding.tvTop, 0, 0);
        animSwipe(binding.tvArabic, 0, 0);
        animSwipe(binding.tvTranslation, 0, 0);
        animSwipe(binding.tvTransliteration, 0, 0);
        animSwipe(binding.tvBottom, 0, 0);
        animSwipe(binding.tvReference, 0, 0);
        animSwipe(binding.tvCurrentSegment, 0, 0);
        totalSwipeDistance = 0;
    }

    private void animSwipe(TextView view, int minus, float total) {
        if (total == 0 && minus == 0) {
            view.animate().translationX(0).setDuration(300).start();
        } else if (total > 0) {
            view.setTranslationX(Math.max(total - minus, 0));
        } else {
            view.setTranslationX(Math.min(total + minus, 0));
        }


    }

    private void setDua(DuaDetailsWithTitle dua, int direction, boolean forceNoDelay) {
        Log.d("hbhb", "setDua: setting dua of : " + dua.getSegmentIndex());

        binding.lx.setMinimumHeight(binding.getRoot().getHeight());

        if (dua != null) {
            this.dua = dua;

            binding.tvTitle.setText(HbUtils.getHtmlText(dua.getTitle()));

            String dsi = "(" + dua.getDua_global_id() + (dua.getDua_segment_id().length() > 0 ? "." : "") + dua.getDua_segment_id() + ")<br/> " + (dua.getSegmentIndex() + 1) + (duaDetailsWithTitleList.size() > 0 ? "/" + duaDetailsWithTitleList.size() : "") + " ";

            checkFavorite(dua, false, false);

            switch (direction) {
                case DIRECTION_UP:
                    animUpSetText(binding.tvTop, HbUtils.getHtmlText(dua.getTop()), 0);
                    animUpSetText(binding.tvArabic, HbUtils.getHtmlText(dua.getArabic()), 100);
                    animUpSetText(binding.tvTranslation, HbUtils.getHtmlText(dua.getTranslations()), 200);
                    animUpSetText(binding.tvTransliteration, HbUtils.getHtmlText(dua.getTransliteration()), 300);
                    animUpSetText(binding.tvBottom, HbUtils.getHtmlText(dua.getBottom()), 400);
                    animUpSetText(binding.tvReference, HbUtils.getHtmlText(dua.getReference()), 500);
                    animUpSetText(binding.tvCurrentSegment, HbUtils.getHtmlText(dsi), 0);
                    break;
                case DIRECTION_LEFT:
                    animLeftSetText(binding.tvTop, HbUtils.getHtmlText(dua.getTop()), 0, forceNoDelay);
                    animLeftSetText(binding.tvArabic, HbUtils.getHtmlText(dua.getArabic()), 100, forceNoDelay);
                    animLeftSetText(binding.tvTranslation, HbUtils.getHtmlText(dua.getTranslations()), 200, forceNoDelay);
                    animLeftSetText(binding.tvTransliteration, HbUtils.getHtmlText(dua.getTransliteration()), 300, forceNoDelay);
                    animLeftSetText(binding.tvBottom, HbUtils.getHtmlText(dua.getBottom()), 400, forceNoDelay);
                    animLeftSetText(binding.tvReference, HbUtils.getHtmlText(dua.getReference()), 500, forceNoDelay);
                    animLeftSetText(binding.tvCurrentSegment, HbUtils.getHtmlText(dsi), 0, forceNoDelay);
                    break;
                case DIRECTION_RIGHT:
                    animRightSetText(binding.tvTop, HbUtils.getHtmlText(dua.getTop()), 0, forceNoDelay);
                    animRightSetText(binding.tvArabic, HbUtils.getHtmlText(dua.getArabic()), 100, forceNoDelay);
                    animRightSetText(binding.tvTranslation, HbUtils.getHtmlText(dua.getTranslations()), 200, forceNoDelay);
                    animRightSetText(binding.tvTransliteration, HbUtils.getHtmlText(dua.getTransliteration()), 300, forceNoDelay);
                    animRightSetText(binding.tvBottom, HbUtils.getHtmlText(dua.getBottom()), 400, forceNoDelay);
                    animRightSetText(binding.tvReference, HbUtils.getHtmlText(dua.getReference()), 500, forceNoDelay);
                    animRightSetText(binding.tvCurrentSegment, HbUtils.getHtmlText(dsi), 0, forceNoDelay);
                    break;
                default:
            }


            DbUtils.addToHistory(getApplicationContext(), Integer.parseInt(dua.getDua_global_id()), dua.getDua_segment_id());

        }
    }

    private void animatedSetFavText(boolean isFav) {
        Animation aso = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_scale_out);

        aso.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (isFav) {

                    Log.d(TAG, "checkFavorite: scroll position " + binding.mcv.getScrollY());

                    binding.ivDeleteFav.setVisibility(View.VISIBLE);
                    binding.tvBtnDeleteFav.setVisibility(View.VISIBLE);
                    binding.tvBtnFav.setText(getResources().getString(R.string.str_added_to_fav));
                    binding.ivBtnFav.setImageResource(R.drawable.baseline_done_all_24);
                    binding.btnAddToFavorite.setOnClickListener(v -> {
                        DbUtils.removeFromFavorite(getApplicationContext(), Integer.parseInt(dua.getDua_global_id()), dua.getDua_segment_id());
                        checkFavorite(dua, true, false);
                    });


                } else {

                    binding.ivDeleteFav.setVisibility(View.GONE);
                    binding.tvBtnDeleteFav.setVisibility(View.GONE);
                    binding.tvBtnFav.setText(getResources().getString(R.string.add_to_favorite));
                    binding.ivBtnFav.setImageResource(R.drawable.baseline_favorite_24);
                    binding.btnAddToFavorite.setOnClickListener(v -> {
                        if (dua.getDua_segment_id().trim().isEmpty()) {

                            DbUtils.addToFavorite(getApplicationContext(), Integer.parseInt(dua.getDua_global_id()), dua.getDua_segment_id());
                            checkFavorite(dua, true, true);
                        } else {
                            DialogSegmentFavoriteBinding sb = DialogSegmentFavoriteBinding.inflate(getLayoutInflater(), null, false);
                            sb.tvTitle.setText(String.format(getResources().getString(R.string.segment_found), dua.getTitle()));
                            sb.tvDesctiption.setText(String.format(getResources().getString(R.string.segment_fav_warning), dua.getTitle()));

//                            HbUtils.blurViews(DuaDetailsActivity.this, 5f, sb.blurMain);

                            AlertDialog ad = new AlertDialog.Builder(DuaDetailsActivity.this)
                                    .setView(sb.getRoot())
                                    .setCancelable(false)
                                    .create();
                            ad.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_transparent, null));
                            sb.btnGo.setOnClickListener(v1 -> {
                                ad.dismiss();
                                DbUtils.addToFavorite(getApplicationContext(), Integer.parseInt(dua.getDua_global_id()), dua.getDua_segment_id());
                                checkFavorite(dua, true, true);
                            });
                            ad.show();
                        }
                    });

                }
                Animation aeo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_scale_in);
                binding.btnAddToFavorite.startAnimation(aeo);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.btnAddToFavorite.startAnimation(aso);

    }

    private void checkFavorite(DuaDetailsWithTitle dua, boolean forceInfo, boolean isFavorite) {

        boolean isFav = DbUtils.isDuaInFavorite(getApplicationContext(), dua);


        if (forceInfo) {
            isFav = isFavorite;
        }
        if (binding.mcv.getScrollY() > 0) {
            binding.mcv.smoothScrollTo(0, 0);
        }
        animatedSetFavText(isFav);
        if (isFav) {
            animFav();
        } else {
            animNotFav(false);

        }
    }

    private void animUpSetText(TextView tv, Spanned text, int delay) {
        int height = binding.getRoot().getHeight();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Animation ta = new TranslateAnimation(0, 0, tv.getTranslationY(), Math.negateExact(View.MeasureSpec.getSize(height)));
            ta.setDuration(300);
            ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

            tv.startAnimation(ta);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    tv.setTranslationX(0);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    tv.setText("");

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

    private void animLeftSetText(TextView tv, Spanned text, int delay, boolean forceNoDelay) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {


                // Create the animations
                ObjectAnimator moveLeft = ObjectAnimator.ofFloat(tv, "translationX", tv.getTranslationX(), -screenWidth);
                moveLeft.setDuration(300);
                ObjectAnimator moveRight = ObjectAnimator.ofFloat(tv, "translationX", screenWidth, 0f);
                moveRight.setDuration(300);

                moveLeft.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tv.setText(text);
                    }
                });

                // Create an AnimatorSet to combine the animations
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(moveLeft, moveRight);
                animatorSet.start();
            }
        }, forceNoDelay ? 0 : delay);
        runOnUiThread(() -> {
//            if (forceNoDelay) {
//            } else {
//                new Handler(Looper.getMainLooper()).postDelayed(() -> {
//
//
//                    Animation ta = new TranslateAnimation(tv.getTranslationX(), Math.negateExact(View.MeasureSpec.getSize(width)), 0, 0);
//                    ta.setDuration(300);
//                    ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);
//                    tv.startAnimation(ta);
//                    ta.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                            tv.setTranslationX(0);
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
////                    tv.setText("");
//
//                            Animation tx = new TranslateAnimation(View.MeasureSpec.getSize(width), 0, 0, 0);
//                            tx.setDuration(600);
//                            tx.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);
//
//                            tv.startAnimation(tx);
//                            tx.setAnimationListener(new Animation.AnimationListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//                                    tv.setText(text);
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animation animation) {
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animation animation) {
//
//                                }
//                            });
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//
//                }, delay);
//            }
//

        });


    }

    private void animRightSetText(TextView tv, Spanned text, int delay, boolean forceNoDelay) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {


                ObjectAnimator moveRight = ObjectAnimator.ofFloat(tv, "translationX", tv.getTranslationX(), screenWidth);
                moveRight.setDuration(300);
                // Create the animations
                ObjectAnimator moveLeft = ObjectAnimator.ofFloat(tv, "translationX", -screenWidth, 0f);
                moveLeft.setDuration(300);

                moveRight.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tv.setText(text);
                    }
                });

                // Create an AnimatorSet to combine the animations
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(moveRight, moveLeft);
                animatorSet.start();
            }
        }, forceNoDelay ? 0 : delay);
/*

        runOnUiThread(() -> {

            if (forceNoDelay) {

                ObjectAnimator moveRight = ObjectAnimator.ofFloat(tv, "translationX", tv.getTranslationX(), screenWidth);
                moveRight.setDuration(300);
                // Create the animations
                ObjectAnimator moveLeft = ObjectAnimator.ofFloat(tv, "translationX", -screenWidth, 0f);
                moveLeft.setDuration(300);

                // Create an AnimatorSet to combine the animations
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(moveRight, moveLeft);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        if(animation.equals(moveRight)){
                            tv.setText(text);
                        }

                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {

                    }
                });
                animatorSet.start();
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    Animation ta = new TranslateAnimation(tv.getTranslationX(), View.MeasureSpec.getSize(screenWidth), 0, 0);
                    ta.setDuration(300);
                    ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

                    tv.startAnimation(ta);
                    ta.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            tv.setTranslationX(0);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
//                    tv.setText("");

                            Animation tx = new TranslateAnimation(Math.negateExact(View.MeasureSpec.getSize(screenWidth)), 0, 0, 0);
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


        });
*/

    }

    private void animFav() {
        if (binding.ivFavorite.getTranslationY() < 0) {

            Animation ta = new TranslateAnimation(0, 0, Math.negateExact(View.MeasureSpec.getSize(1000)), 0);
            ta.setDuration(600);
            ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

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

            binding.ivFavorite.startAnimation(ta);
        } else animNotFav(true);

    }

    private void animNotFav(boolean runFavOnEnd) {

        if (binding.ivFavorite.getTranslationY() == 0) {

            Animation ta = new TranslateAnimation(0, 0, 0, Math.negateExact(View.MeasureSpec.getSize(1000)));
            ta.setDuration(400);
            ta.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);

            ta.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.ivFavorite.setTranslationY(Math.negateExact(View.MeasureSpec.getSize(1000)));


                    if (runFavOnEnd) {
                        animFav();
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

            });

            binding.ivFavorite.startAnimation(ta);
        }
    }


}