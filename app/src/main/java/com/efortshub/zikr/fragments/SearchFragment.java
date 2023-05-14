package com.efortshub.zikr.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efortshub.zikr.R;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;

public class SearchFragment extends Fragment {
    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance(BottomNavAnimationListener listener) {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}