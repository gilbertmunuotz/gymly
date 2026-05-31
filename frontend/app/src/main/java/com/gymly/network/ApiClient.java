package com.gymly.network;

import com.gymly.data.api.AuthApiService;
import com.gymly.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static Retrofit retrofit;
    private static AuthApiService authApiService;
    private static String authToken;

    private ApiClient() {
    }

    public static void setAuthToken(String token) {
        authToken = token;
        retrofit = null;
        authApiService = null;
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        okhttp3.Request.Builder requestBuilder = chain.request().newBuilder();
                        if (authToken != null && !authToken.isEmpty()) {
                            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
                        }
                        return chain.proceed(requestBuilder.build());
                    })
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static AuthApiService getAuthService() {
        if (authApiService == null) {
            authApiService = getRetrofit().create(AuthApiService.class);
        }
        return authApiService;
    }
}
