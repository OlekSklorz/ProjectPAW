package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by olek1 on 03.04.2017.
 */

/**
 * Object of <code>Laguanges</code> class represents all available languages for this application.
 * Languages are stored in map, in which languages are keys and codes are values.
 */
public class Languages {
    private static Map<String, String> languages;
    public Languages(Activity activity){
        Resources resources = activity.getResources();
        String[] languagesArray = resources.getStringArray(R.array.languages);
        languages = new HashMap(languagesArray.length);
        for(int i = 0; i < languagesArray.length; i++){
            if(languagesArray[i].equalsIgnoreCase(resources.getString(R.string.english)))
                languages.put(languagesArray[i], "en");
            if(languagesArray[i].equalsIgnoreCase(resources.getString(R.string.polish)))
                languages.put(languagesArray[i], "pl");
        }
    }

    /**
     * Set localization for choosen language.
     * @param language choosen language
     * @param resources resources
     */
    public static void setLocalization(String language, Resources resources){
        Locale locale = new Locale(getCode(language));
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.setLocale(locale);
        resources.updateConfiguration(conf, metrics);
    }

    private static String getCode(String language){
        for(Map.Entry<String, String> entry : languages.entrySet()) {
            Log.d("JESTEM", entry.getKey());
            if (entry.getKey().equalsIgnoreCase(language))
                return entry.getValue();
        }
        return null;
    }
}
