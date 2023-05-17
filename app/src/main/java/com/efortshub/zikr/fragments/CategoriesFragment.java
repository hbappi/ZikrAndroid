package com.efortshub.zikr.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efortshub.zikr.R;
import com.efortshub.zikr.activities.CategoryWiseLIstActivity;
import com.efortshub.zikr.activities.DuaDetailsActivity;
import com.efortshub.zikr.activities.MainActivity;
import com.efortshub.zikr.adapter.HistoryItemRvAdapter;
import com.efortshub.zikr.adapter.HistoryItemRvTransparentAdapter;
import com.efortshub.zikr.databinding.FragmentCategoriesBinding;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;
import com.efortshub.zikr.models.Dua;
import com.efortshub.zikr.models.DuaDetails;
import com.efortshub.zikr.models.DuaDetailsWithHistory;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.models.History;
import com.efortshub.zikr.utils.DbUtils;
import com.efortshub.zikr.utils.HbConsts;
import com.efortshub.zikr.utils.HbUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {

    private static BottomNavAnimationListener listener;
    FragmentCategoriesBinding binding;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance(BottomNavAnimationListener listener) {
        CategoriesFragment.listener = listener;
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        loadRandomDua();
        loadHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

//        blurViews(binding.blur1);

        HbUtils.blurViews(requireActivity(),15f,  binding.blurMain);

        startAnimationLoop(R.anim.anim_fade_out, 0,
                binding.btnBedtime,
                binding.btnMorningEvening,
                binding.btnSocialManner,
                binding.btnFamily,
                binding.btnHajj,
                binding.btnPurification,
                binding.btnPrayer,
                binding.btnRamadan,
                binding.btnFoodDrinks,
                binding.btnRizq,
                binding.btnTravel,
                binding.btnIllness,
                binding.btnSeekingRefuge,
                binding.btnConfession,
                binding.btnNature
        );



        return binding.getRoot();
    }


    private void loadHistory() {

        List<History> hs = DbUtils.getAllHistory(requireContext());
        int size = Math.min(hs.size(), 5);
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
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


        HistoryItemRvTransparentAdapter adapter = new HistoryItemRvTransparentAdapter(duaDetailsWithTitleList, details -> {
            Intent i = new Intent(requireActivity(), DuaDetailsActivity.class);
            i.putExtra("dua", details);
            i.putExtra("full", false);
            i.putExtra("list", array);

            startActivity(i);

        });
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
        animAdapter.setDuration(500);
        animAdapter.setStartPosition(0);
        animAdapter.setInterpolator(new OvershootInterpolator(2f));
        animAdapter.setStartPosition(0);
        animAdapter.setFirstOnly(false);
        animAdapter.setHasStableIds(false);
        binding.rvHistory.setAdapter(animAdapter);



    }

    private void setOpenDetailsListener(MaterialCardView btn, int[] arr, String title) {
        btn.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), CategoryWiseLIstActivity.class);
            i.putExtra("list", arr);
            i.putExtra("full", false);
            i.putExtra("title", title);

            startActivity(i);
        });

    }


    private void startAnimationLoop(int res, int startFrom, View... view) {

        Animation a = AnimationUtils.loadAnimation(requireContext(), res);
        if (startFrom == 0) {
            for (View v : view) {
                v.setAlpha(0f);
            }
        }
        Log.d("hbhb", "startAnimationWithEndListener: " + startFrom);
        if (startFrom == view.length) {
            listener.shouldShowBottomNavNow();
            initFunctions();
        }
        if (view.length > startFrom) {
            view[startFrom].startAnimation(a);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view[startFrom].setAlpha(1f);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        startAnimationLoop(res, startFrom + 1, view);
                        Log.d("hbhb", "onAnimationEnd: " + startFrom);
                    }, 50);

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    @SuppressLint("ResourceType")
    private void initFunctions() {

        if (HbUtils.getLanguageCode(requireContext()).equals("bn")) {

            setOpenDetailsListener(binding.btnBedtime, HbConsts.CL_BN_SLEEPING, getResources().getString(R.string.bedtime));
            setOpenDetailsListener(binding.btnMorningEvening, HbConsts.CL_BN_MORNING_EVENING, getResources().getString(R.string.morning_evening));
            setOpenDetailsListener(binding.btnSocialManner, HbConsts.CL_BN_SOCIAL, getResources().getString(R.string.social_manner));
            setOpenDetailsListener(binding.btnFamily, HbConsts.CL_BN_FAMILY, getResources().getString(R.string.family));
            setOpenDetailsListener(binding.btnHajj, HbConsts.CL_BN_HAJJ, getResources().getString(R.string.hajj));
            setOpenDetailsListener(binding.btnPurification, HbConsts.CL_BN_PURIFICATION, getResources().getString(R.string.purification));
            setOpenDetailsListener(binding.btnPrayer, HbConsts.CL_BN_PRAYER, getResources().getString(R.string.prayer));
            setOpenDetailsListener(binding.btnRamadan, HbConsts.CL_BN_RAMADAN, getResources().getString(R.string.ramadan_fasting));
            setOpenDetailsListener(binding.btnFoodDrinks, HbConsts.CL_BN_FOOD_DRINK, getResources().getString(R.string.food_amp_drinks));
            setOpenDetailsListener(binding.btnRizq, HbConsts.CL_BN_PROVISION, getResources().getString(R.string.possession_rizk));
            setOpenDetailsListener(binding.btnTravel, HbConsts.CL_BN_TRAVEL, getResources().getString(R.string.travel));
            setOpenDetailsListener(binding.btnIllness, HbConsts.CL_BN_SICKNESS, getResources().getString(R.string.illness_death));
            setOpenDetailsListener(binding.btnSeekingRefuge, HbConsts.CL_BN_REFUGE, getResources().getString(R.string.seeking_refuge));
            setOpenDetailsListener(binding.btnConfession, HbConsts.CL_BN_GRATITUDE, getResources().getString(R.string.gratitude_confession));
            setOpenDetailsListener(binding.btnNature, HbConsts.CL_BN_NATURE, getResources().getString(R.string.nature));

        } else {

            setOpenDetailsListener(binding.btnBedtime, HbConsts.CL_EN_SLEEPING, getResources().getString(R.string.bedtime));
            setOpenDetailsListener(binding.btnMorningEvening, HbConsts.CL_EN_MORNING_EVENING, getResources().getString(R.string.morning_evening));
            setOpenDetailsListener(binding.btnSocialManner, HbConsts.CL_EN_SOCIAL, getResources().getString(R.string.social_manner));
            setOpenDetailsListener(binding.btnFamily, HbConsts.CL_EN_FAMILY, getResources().getString(R.string.family));
            setOpenDetailsListener(binding.btnHajj, HbConsts.CL_EN_HAJJ, getResources().getString(R.string.hajj));
            setOpenDetailsListener(binding.btnPurification, HbConsts.CL_EN_PURIFICATION, getResources().getString(R.string.purification));
            setOpenDetailsListener(binding.btnPrayer, HbConsts.CL_EN_PRAYER, getResources().getString(R.string.prayer));
            setOpenDetailsListener(binding.btnRamadan, HbConsts.CL_EN_RAMADAN, getResources().getString(R.string.ramadan_fasting));
            setOpenDetailsListener(binding.btnFoodDrinks, HbConsts.CL_EN_FOOD_DRINK, getResources().getString(R.string.food_amp_drinks));
            setOpenDetailsListener(binding.btnRizq, HbConsts.CL_EN_PROVISION, getResources().getString(R.string.possession_rizk));
            setOpenDetailsListener(binding.btnTravel, HbConsts.CL_EN_TRAVEL, getResources().getString(R.string.travel));
            setOpenDetailsListener(binding.btnIllness, HbConsts.CL_EN_SICKNESS, getResources().getString(R.string.illness_death));
            setOpenDetailsListener(binding.btnSeekingRefuge, HbConsts.CL_EN_REFUGE, getResources().getString(R.string.seeking_refuge));
            setOpenDetailsListener(binding.btnConfession, HbConsts.CL_EN_GRATITUDE, getResources().getString(R.string.gratitude_confession));
            setOpenDetailsListener(binding.btnNature, HbConsts.CL_EN_NATURE, getResources().getString(R.string.nature));

        }
    }

    private void loadRandomDua() {

        Random rand = new Random();
        Dua dua = HbUtils.getDuaOfIndex(requireContext(), Math.max(1, rand.nextInt(Objects.equals(HbUtils.getLanguageCode(requireContext()), "bn") ? 422 : 328)));

        List<DuaDetails> details = dua.getDetails();

        int dind = rand.nextInt(details.size());

        if (details.get(dind).getTranslations().trim().isEmpty()) {
            loadRandomDua();
            return;
        }

        binding.tvDailyDuaLocale.setText(details.get(dind).getTranslations());
        binding.tvDailyDuaFootnote.setText(details.get(dind).getReference());


    }
}