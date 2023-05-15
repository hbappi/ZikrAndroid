package com.efortshub.zikr.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;

import com.efortshub.zikr.models.Dua;
import com.efortshub.zikr.models.DuaDetails;
import com.efortshub.zikr.models.DuaDetailsWithTitle;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class HbUtils {

    public static String getLanguageCode(Context context) {

        String code = context.getSharedPreferences(HbConsts.KEY_SAHRED_PREF, MODE_PRIVATE)
                .getString(HbConsts.KEY_SP_LANGUAGE_CODE, "en");
        return code;
    }

    public static Dua getDuaOfIndex(Context context, Integer index) {
        String result = "";

        String code = context.getSharedPreferences(HbConsts.KEY_SAHRED_PREF, MODE_PRIVATE)
                .getString(HbConsts.KEY_SP_LANGUAGE_CODE, "en");

        BufferedReader bufferedReader = null;

        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open(code + "/" + index + ".json"), "UTF-8");
            bufferedReader = new BufferedReader(isr);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result = result + line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        Gson gson = new Gson();
        Dua dua = gson.fromJson(result, Dua.class);
//        Log.d("hbhb", "onCreate: " + dua.getPageTitle() + " - " + dua.getDetails().get(0).getTranslations());
        return dua;

    }

    public static List<DuaDetailsWithTitle> getAllDua(Context context) {
        List<DuaDetailsWithTitle> duaList = new ArrayList<>();
        int size = 328;
        if (getLanguageCode(context).equals("bn")) {
            size = 422;
        }

        int index = 0;
        for (int i = 1; i <= size; i++) {
            Dua dua = getDuaOfIndex(context, i);

            for (DuaDetails dd : dua.getDetails()) {
                duaList.add(new DuaDetailsWithTitle(dd.getDua_segment_id(), dd.getTop(), dd.getArabic_diacless(), dd.getArabic(), dd.getTransliteration(), dd.getTranslations(), dd.getBottom(), dd.getReference(), dd.getDua_global_id(),dua.getPageTitle(), index++));
            }
        }

        return duaList;


    }
}
