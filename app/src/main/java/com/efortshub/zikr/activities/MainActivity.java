package com.efortshub.zikr.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.efortshub.zikr.R;
import com.efortshub.zikr.databinding.ActivityMainBinding;
import com.efortshub.zikr.fragments.CategoriesFragment;
import com.efortshub.zikr.utils.HbConsts;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FragmentManager fm = getSupportFragmentManager();
    Fragment oldFragment;
    String languageCode = "en";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String code = getSharedPreferences(HbConsts.KEY_SAHRED_PREF, MODE_PRIVATE)
                .getString(HbConsts.KEY_SP_LANGUAGE_CODE, "en");
        languageCode = code;
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFragment(CategoriesFragment.newInstance());

        binding.tvChangeLanguage.setText(Objects.equals(languageCode, "bn") ? "বাংলা" : "English");


        binding.btnChangeLanguage.setOnClickListener((v) -> {
            if (languageCode.equals("bn")) {
                getSharedPreferences(HbConsts.KEY_SAHRED_PREF, MODE_PRIVATE)
                        .edit()
                        .putString(HbConsts.KEY_SP_LANGUAGE_CODE, "en").apply();

            } else {
                getSharedPreferences(HbConsts.KEY_SAHRED_PREF, MODE_PRIVATE)
                        .edit()
                        .putString(HbConsts.KEY_SP_LANGUAGE_CODE, "bn").apply();

            }
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();

        });


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