package com.example.myapplication.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.models.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_pref";

    private static SharedPrefManager Instance;
    private Context ctx;

    public SharedPrefManager(Context ctx) {
        this.ctx = ctx;
    }

    public static synchronized SharedPrefManager getInstance(Context ctx) {
        if (Instance == null)
            Instance = new SharedPrefManager(ctx);
        return Instance;
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("ID", user.getId());
        editor.putString("Login", user.getLogin());

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ID", -1) != -1;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("ID", -1),
                sharedPreferences.getString("Login", null)
        );
    }

    public void Clear(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
