package com.efortshub.zikr.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.efortshub.zikr.R;
import com.efortshub.zikr.databinding.ActivityMainBinding;
import com.efortshub.zikr.fragments.CategoriesFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FragmentManager fm = getSupportFragmentManager();
    Fragment oldFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFragment(CategoriesFragment.newInstance());

        binding.bnavMain.setOnItemSelectedListener(item -> {

            Log.d("hbhb", "onCreate: onn bnav selected: " + item.getTitle() + " " + (item.getItemId() == R.id.nv_categories));

            if (item.getItemId() == R.id.nv_categories) {
                setFragment(CategoriesFragment.newInstance());
            }

            return false;


        });


    }


    void setFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();

        if (oldFragment == null) {
            ft.replace(R.id.frame_layout_main, fragment, "ft");
        } else if (!fragment.isAdded()) {
            ft.hide(oldFragment).add(fragment, "ft");
        } else {
            ft.hide(oldFragment).show(fragment);
        }

        ft.commit();
    }
}