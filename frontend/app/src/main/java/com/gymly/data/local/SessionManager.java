package com.gymly.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.gymly.network.ApiClient;
import com.gymly.utils.Constants;

public class SessionManager {

    private static SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SessionManager not initialized");
        }
        return instance;
    }

    public void saveSession(String token, long userId, String userName, String email) {
        prefs.edit()
                .putString(Constants.KEY_AUTH_TOKEN, token)
                .putLong(Constants.KEY_USER_ID, userId)
                .putString(Constants.KEY_USER_NAME, userName)
                .putString(Constants.KEY_USER_EMAIL, email)
                .apply();
        ApiClient.setAuthToken(token);
    }

    public boolean isLoggedIn() {
        String token = prefs.getString(Constants.KEY_AUTH_TOKEN, null);
        return token != null && !token.isEmpty();
    }

    public String getToken() {
        return prefs.getString(Constants.KEY_AUTH_TOKEN, null);
    }

    public long getUserId() {
        return prefs.getLong(Constants.KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(Constants.KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return prefs.getString(Constants.KEY_USER_EMAIL, "");
    }

    public void restoreToken() {
        String token = getToken();
        if (token != null) {
            ApiClient.setAuthToken(token);
        }
    }

    public void clearSession() {
        prefs.edit().clear().apply();
        ApiClient.setAuthToken(null);
    }
}
