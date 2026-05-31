package com.gymly.utils;

import com.gymly.data.model.ApiResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

public final class ApiUtils {

    private ApiUtils() {
    }

    public static String getErrorMessage(Response<?> response, String fallback) {
        try {
            if (response.errorBody() != null) {
                Gson gson = new Gson();
                ApiResponse<?> error = gson.fromJson(response.errorBody().string(), ApiResponse.class);
                if (error != null && error.getMessage() != null && !error.getMessage().isEmpty()) {
                    return error.getMessage();
                }
            }
        } catch (IOException ignored) {
            // fall through to fallback
        }
        return fallback;
    }
}
