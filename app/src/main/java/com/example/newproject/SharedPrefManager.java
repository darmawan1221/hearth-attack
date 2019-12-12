package com.example.newproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Belal on 9/5/2017.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_ID = "id";
    private static final String KEY_AGE = "age";
    private static final String KEY_NAME = "name";
    private static final String KEY_MARRIAGE = "marriage";
    private static final String KEY_DATE = "date_birth";
    private static final String KEY_POSITION = "position";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_NIK = "nik_npk";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(Register_con user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_AGE, user.getAge());
        editor.putString(KEY_MARRIAGE, user.getMarriage());
        editor.putString(KEY_DATE, user.getDate());
        editor.putString(KEY_POSITION, user.getPosition());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_NIK, user.getNik());
        editor.apply();
    }

    //this method will give the logged in user

    public void save_array(String key,ArrayList list){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<String>(list);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, set);
        editor.apply();
    }

    public void save_string(String key,String list){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, list);
        editor.apply();
    }

    public String get_string(String key){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public ArrayList<String> get_array(String key){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet(key, null);
        ArrayList<String> list = new ArrayList<>(set);
        return list;
    }

    public Register_con getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Register_con(
                sharedPreferences.getString(KEY_AGE, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_MARRIAGE, null),
                sharedPreferences.getString(KEY_DATE, null),
                sharedPreferences.getString(KEY_POSITION, null),
                sharedPreferences.getString(KEY_GENDER, null),
                sharedPreferences.getString(KEY_NIK, null)
        );
    }
}