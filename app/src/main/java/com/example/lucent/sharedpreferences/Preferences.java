package com.example.lucent.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    /** Key-data declaration in the form of a String, to serve as a data storage container.
        So each data has a different key from each other */
    static final String KEY_USER_REGISTER ="user", KEY_PASS_REGISTER ="pass";
    static final String KEY_USERNAME_CURRENTLY_LOGIN = "Username_logged_in";
    static final String KEY_STATUS_CURRENTLY_LOGIN = "Status_logged_in";

    /** Shared Preferences declaration based on context parameter */
    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /** Edit Preferences declaration and change data
        which has key contents KEY_USER_REGISTER with username parameter */
    public static void setRegisteredUser(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USER_REGISTER, username);
        editor.apply();
    }
    /** Returns the value of the KEY_USER_REGISTER key as a String */
    public static String getRegisteredUser(Context context){
        return getSharedPreference(context).getString(KEY_USER_REGISTER,"");
    }

    /** Edit Preferences declaration and change data
        which has a key KEY_PASS_REGISTER with a password parameter */
    public static void setRegisteredPass(Context context, String password){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_PASS_REGISTER, password);
        editor.apply();
    }
    /** Returns the value of the KEY_PASS_REGISTER key as a String */
    public static String getRegisteredPass(Context context){
        return getSharedPreference(context).getString(KEY_PASS_REGISTER,"");
    }

    /** Edit Preferences declaration and change data
        which has key KEY_USERNAME_CURRENTLY_LOGIN with username parameter */
    public static void setLoggedInUser(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USERNAME_CURRENTLY_LOGIN, username);
        editor.apply();
    }
    /** Returns the value of the KEY_USERNAME_CURRENTLY_LOGIN key as a String */
    public static String getLoggedInUser(Context context){
        return getSharedPreference(context).getString(KEY_USERNAME_CURRENTLY_LOGIN,"");
    }

    /** Edit Preferences declaration and change data
       which has key KEY_STATUS_CURRENTLY_LOGIN with status parameter */
    public static void setLoggedInStatus(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_CURRENTLY_LOGIN,status);
        editor.apply();
    }
    /** Returns the value of the KEY_STATUS_CURRENTLY_LOGIN key as a boolean */
    public static boolean getLoggedInStatus(Context context){
        return getSharedPreference(context).getBoolean(KEY_STATUS_CURRENTLY_LOGIN,false);
    }

    /** Edit Preferences declaration and delete data, making it the default value
     specifically for data that has keys KEY_USERNAME_CURRENTLY_LOGIN and KEY_STATUS_CURRENTLY_LOGIN */
    public static void clearLoggedInUser (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USERNAME_CURRENTLY_LOGIN);
        editor.remove(KEY_STATUS_CURRENTLY_LOGIN);
        editor.apply();
    }
}