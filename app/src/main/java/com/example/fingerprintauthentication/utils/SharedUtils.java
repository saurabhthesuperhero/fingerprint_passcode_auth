package com.example.fingerprintauthentication.utils;

import android.content.Context;

public class SharedUtils {
    private static final String PREF_APP = "pref_app";

    public void storeFingerAuth(Context context, Boolean bool) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putBoolean("fingerAuth", bool).apply();
    }

    public void storePassAuth(Context context, Boolean bool) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putBoolean("passAuth", bool).apply();
    }

    public void deviceSupportFingerprint(Context context, Boolean bool) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putBoolean("fingerSupport", bool).apply();

    }

    public Boolean getPassAuth(Context context) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean("passAuth", false);
    }

    public Boolean getFingerSupport(Context context) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean("fingerSupport", false);
    }

    public Boolean getFingerAuth(Context context) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean("fingerAuth", false);
    }

}
