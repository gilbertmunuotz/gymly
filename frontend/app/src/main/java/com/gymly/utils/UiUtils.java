package com.gymly.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.gymly.R;
import com.google.android.material.snackbar.Snackbar;

public final class UiUtils {

    private UiUtils() {
    }

    public static void showError(View anchor, String message) {
        if (anchor != null) {
            Snackbar.make(anchor, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void showNetworkError(View anchor, Context context) {
        showError(anchor, context.getString(R.string.error_network));
    }

    public static void showSuccess(Context context, @StringRes int messageRes) {
        Toast.makeText(context, messageRes, Toast.LENGTH_SHORT).show();
    }

    public static void showSuccess(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
