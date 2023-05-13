package com.efortshub.zikr.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;

import com.efortshub.zikr.models.Dua;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HbUtils {

    public static String getLanguageCode(Context context){

        String code = context.getSharedPreferences(HbConsts.KEY_SAHRED_PREF, MODE_PRIVATE)
                .getString(HbConsts.KEY_SP_LANGUAGE_CODE, "en");
        return code;
    }
    public static Dua getauDuaOfIndex(Context context, Integer index) {
        String result = "";

        String code = context.getSharedPreferences(HbConsts.KEY_SAHRED_PREF, MODE_PRIVATE)
                .getString(HbConsts.KEY_SP_LANGUAGE_CODE, "en");

        BufferedReader bufferedReader = null;

        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open( code + "/" + index + ".json"), "UTF-8");
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
        Log.d("hbhb", "onCreate: "+ dua.getPageTitle()+" - "+ dua.getDetails().get(0).getTranslations());
        return dua;

    }
}
