package com.efortshub.zikr.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efortshub.zikr.databinding.FragmentCategoriesBinding;
import com.efortshub.zikr.models.Dua;
import com.efortshub.zikr.models.DuaDetails;
import com.efortshub.zikr.utils.HbUtils;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {

    FragmentCategoriesBinding binding;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance() {
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

        loadRandomDua();




        return binding.getRoot();
    }

    private void loadRandomDua() {

        Random rand = new Random();
        Dua dua = HbUtils.getauDuaOfIndex(requireContext(), Math.max(1, rand.nextInt(Objects.equals(HbUtils.getLanguageCode(requireContext()), "bn") ? 422 : 328)));

        List<DuaDetails> details = dua.getDetails();

        int dind = rand.nextInt(details.size());

        if(details.get(dind).getTranslations().trim().isEmpty()){
            loadRandomDua();
            return;
        }

        binding.tvDailyDuaLocale.setText(details.get(dind).getTranslations());
        binding.tvDailyDuaFootnote.setText(details.get(dind).getReference());


    }
}