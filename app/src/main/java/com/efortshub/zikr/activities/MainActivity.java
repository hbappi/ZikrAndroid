package com.efortshub.zikr.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import com.efortshub.zikr.R;
import com.efortshub.zikr.adapter.HistoryItemRvAdapter;
import com.efortshub.zikr.databinding.ActivityMainBinding;
import com.efortshub.zikr.fragments.AllItemFragment;
import com.efortshub.zikr.fragments.CategoriesFragment;
import com.efortshub.zikr.fragments.FavoriteFragment;
import com.efortshub.zikr.fragments.SearchFragment;
import com.efortshub.zikr.interfaces.BottomNavAnimationListener;
import com.efortshub.zikr.models.DuaDetailsWithHistory;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.efortshub.zikr.models.History;
import com.efortshub.zikr.utils.DbHelper;
import com.efortshub.zikr.utils.DbUtils;
import com.efortshub.zikr.utils.HbConsts;
import com.efortshub.zikr.utils.HbUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FragmentManager fm = getSupportFragmentManager();
    Fragment oldFragment;
    String languageCode = "en";
    boolean bottomNavEnabled = true;


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
        HbUtils.blurViews(MainActivity.this, 20f, binding.blurMain);
        binding.blurMain.setVisibility(View.GONE);

        initInfoButtons();


        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String v = "version: "+packageInfo.versionName+" ("+packageInfo.versionCode+")";
            binding.tvVersion.setText(v);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        binding.tvOpensourceList.setText(HbConsts.openSourceList);


        setFragment(CategoriesFragment.newInstance(this::showBottomNav));

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

            if (bottomNavEnabled) {
                if (item.getItemId() == R.id.nv_categories) {
                    setFragment(CategoriesFragment.newInstance(this::showBottomNav));
                    binding.blurMain.setVisibility(View.GONE);
                    return true;
                } else if (item.getItemId() == R.id.nv_all) {
                    setFragment(AllItemFragment.newInstance(this::showBottomNav));
                    binding.blurMain.setVisibility(View.GONE);
                    return true;
                } else if (item.getItemId() == R.id.nv_search) {
                    setFragment(SearchFragment.newInstance(this::showBottomNav));
                    binding.blurMain.setVisibility(View.GONE);
                    return true;
                }else if(item.getItemId() == R.id.nv_favorite){
                    setFragment(FavoriteFragment.newInstance(this::showBottomNav));
                    binding.blurMain.setVisibility(View.GONE);
                    return true;
                }else if(item.getItemId()==R.id.nv_settings){
                    binding.blurMain.setVisibility(View.VISIBLE);
                    return true;
                }
            }

            return bottomNavEnabled;

        });






    }

    private void initInfoButtons() {

        bindButtonwithLink(binding.btnEfortshub, HbConsts.linkEfortshubWebsite);
        bindButtonwithLink(binding.btnEfortshubGithub, HbConsts.linkEfortshubGithub);
        bindButtonwithLink(binding.btnEfortshubFacebook, HbConsts.linkEfortshubFacebook);
        bindButtonwithLink(binding.btnEfortshubYoutube, HbConsts.linkEfortshubYoutube);
        bindButtonwithLink(binding.btnEfortshubLinkedin, HbConsts.linkEfortshubLinkedIn);
        bindButtonwithLink(binding.btnEfortshubInstagram, HbConsts.linkEfortshubInstagram);
        bindButtonwithLink(binding.btnEfortshubTumblr, HbConsts.linkEfortshubTumblr);
        bindButtonwithLink(binding.btnEfortshubTwitter, HbConsts.linkEfortshubTwitter);


        bindButtonwithLink(binding.btnHbappiGithub, HbConsts.linkHbappiGithub);
        bindButtonwithLink(binding.btnHbappiFacebook, HbConsts.linkHbappiFacebook);
        bindButtonwithLink(binding.btnHbappiYoutube, HbConsts.linkHbappiYoutube);
        bindButtonwithLink(binding.btnHbappiLinkedin, HbConsts.linkHbappiLinkedIn);
        bindButtonwithLink(binding.btnHackerrank, HbConsts.linkHbappiHackerrank);


    }

    private void bindButtonwithLink(Button btn, String link) {
       btn.setOnClickListener(v -> {
           Intent i = new Intent(Intent.ACTION_VIEW);
           i.setData(Uri.parse(link));
           startActivity(i);
       });
    }


    private void hideBottomNav() {
        bottomNavEnabled = false;

    }

    private void showBottomNav() {
        bottomNavEnabled = true;
    }

    void setFragment(Fragment fragment) {
        hideBottomNav();
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