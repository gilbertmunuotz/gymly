package com.gymly;

import android.app.Application;

import com.gymly.data.local.SessionManager;

public class GymlyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.init(this);
        SessionManager.getInstance().restoreToken();
    }
}
