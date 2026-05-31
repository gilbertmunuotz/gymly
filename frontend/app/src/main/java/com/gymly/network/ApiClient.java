package com.gymly.network;

import com.gymly.data.api.AuthApiService;
import com.gymly.data.api.CheckInApiService;
import com.gymly.data.api.ClassApiService;
import com.gymly.data.api.MembershipApiService;
import com.gymly.data.api.ProfileApiService;
import com.gymly.data.api.TrainerApiService;
import com.gymly.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static Retrofit retrofit;
    private static AuthApiService authApiService;
    private static MembershipApiService membershipApiService;
    private static ClassApiService classApiService;
    private static TrainerApiService trainerApiService;
    private static CheckInApiService checkInApiService;
    private static ProfileApiService profileApiService;
    private static String authToken;

    private ApiClient() {
    }

    public static void setAuthToken(String token) {
        authToken = token;
        retrofit = null;
        authApiService = null;
        membershipApiService = null;
        classApiService = null;
        trainerApiService = null;
        checkInApiService = null;
        profileApiService = null;
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

    public static MembershipApiService getMembershipService() {
        if (membershipApiService == null) {
            membershipApiService = getRetrofit().create(MembershipApiService.class);
        }
        return membershipApiService;
    }

    public static ClassApiService getClassService() {
        if (classApiService == null) {
            classApiService = getRetrofit().create(ClassApiService.class);
        }
        return classApiService;
    }

    public static TrainerApiService getTrainerService() {
        if (trainerApiService == null) {
            trainerApiService = getRetrofit().create(TrainerApiService.class);
        }
        return trainerApiService;
    }

    public static CheckInApiService getCheckInService() {
        if (checkInApiService == null) {
            checkInApiService = getRetrofit().create(CheckInApiService.class);
        }
        return checkInApiService;
    }

    public static ProfileApiService getProfileService() {
        if (profileApiService == null) {
            profileApiService = getRetrofit().create(ProfileApiService.class);
        }
        return profileApiService;
    }
}
