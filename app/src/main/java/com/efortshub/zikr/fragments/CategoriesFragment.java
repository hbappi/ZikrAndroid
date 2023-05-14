package com.efortshub.zikr.fragments;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.efortshub.zikr.R;
import com.efortshub.zikr.databinding.FragmentCategoriesBinding;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;
import com.efortshub.zikr.models.Dua;
import com.efortshub.zikr.models.DuaDetails;
import com.efortshub.zikr.utils.HbUtils;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

//        blurViews(binding.blur1);


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


        loadRandomDua();

        return binding.getRoot();
    }

    private void blurViews(BlurView... views) {

        for (BlurView v : views) {

            float radius = 20f;

            View decorView = requireActivity().getWindow().getDecorView();
            // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
            ViewGroup rootView = decorView.findViewById(android.R.id.content);

            // Optional:
            // Set drawable to draw in the beginning of each blurred frame.
            // Can be used in case your layout has a lot of transparent space and your content
            // gets a too low alpha value after blur is applied.
            Drawable windowBackground = AppCompatResources.getDrawable(requireContext(), R.drawable.bg);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                v.setupWith(rootView, new RenderEffectBlur()) // or RenderEffectBlur
                        .setFrameClearDrawable(windowBackground) // Optional
                        .setBlurRadius(radius);
            } else {
                v.setupWith(rootView, new RenderScriptBlur(requireContext())) // or RenderEffectBlur
                        .setFrameClearDrawable(windowBackground) // Optional
                        .setBlurRadius(radius);

            }
            v.setOutlineProvider(ViewOutlineProvider.BOUNDS);
            v.setClipToOutline(true);

        }

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