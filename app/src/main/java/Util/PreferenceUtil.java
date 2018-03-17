package Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by 가애 on 2017-12-20.
 */

public final class PreferenceUtil {

    private static PreferenceUtil instance;
    private final SharedPreferences mSharedPreferences;

    final static public String APP_THEME_COLOR = "isThemeColor";
    final static public String MY_INFO = "MyInfo";
    final static public String PASSWORD = "Password";
    final static public String COMPLETE_PASSWORD = "C_Password";


    public PreferenceUtil(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized PreferenceUtil getInstance(Context context) {
        if (null == instance) {
            instance = new PreferenceUtil(context);
        }
        return instance;
    }

    public String getString(String key, String def) {
        return mSharedPreferences.getString(key, def);
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public boolean setString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (null == value) {
            value = "";
        }
        editor.putString(key, value);
        return editor.commit();
    }

    public int getInt(String key, boolean b) {
        return mSharedPreferences.getInt(key, 0);
    }

    public boolean setInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public boolean setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getBoolean(String key, boolean def) {
        return mSharedPreferences.getBoolean(key, def);
    }

    public boolean remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    public boolean clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }
}
