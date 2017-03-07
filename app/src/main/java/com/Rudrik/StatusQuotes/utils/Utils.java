/**
 * @author R3Techno
 * This is common class , common methods and properties will be consume here which can also use in other projects 
 *
 * @warning Don't make empty parameter constructor
 */
package com.Rudrik.StatusQuotes.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;


public class Utils {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // constructor which has to be used in every class to use this class
    // properties
    public Utils(Context con) {
        this.context = con;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    /**
     * Use to save string preferences
     */
    public void setPrefrences(String key, String msg) {
        editor.putString(key, msg);
        editor.commit();
    }

    /**
     * Use to save Int preferences
     */
    public void setIntPrefrences(String key, int position) {
        editor.putInt(key, position);
        editor.commit();
    }

    /**
     * Use to get string preference
     */
    public String getPrefrences(String key) {
        return preferences.getString(key, "");
    }

    /**
     * Use to get Integer preference
     */
    public int getIntPrefrences(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * Use to set boolean preference
     */
    public void setBoolPrefrences(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * use to get boolean preference
     */
    public boolean getBoolPref(String key) {
        return preferences.getBoolean(key, false);
    }

    public void Toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @return IMEI NO
     */
    public String getDeviceId() {
        TelephonyManager teleManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return teleManager.getDeviceId();
    }
}
