package com.gymly.utils;

import com.gymly.BuildConfig;

/**
 * App-wide constants. Values are defined here; API URL comes from BuildConfig.
 */
public final class Constants {

    private Constants() {
    }

    public static final String BASE_URL = BuildConfig.API_BASE_URL;

    // SharedPreferences (Phase 4)
    public static final String PREFS_NAME = "gymly_prefs";
    public static final String KEY_AUTH_TOKEN = "auth_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
}
