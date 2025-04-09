package com.example.pharmiczy.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pharmiczy.DataModels.Medicine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MedicineCache {

    private static final String PREF_NAME = "MedicinePref";
    private static final String KEY_MEDICINES = "medicines";

    public static void saveMedicines(Context context, List<Medicine> medicines) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(medicines);
        editor.putString(KEY_MEDICINES, json);
        editor.apply();
    }

    public static List<Medicine> getCachedMedicines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_MEDICINES, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Medicine>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }
}
