package com.gymly.data.api;

import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.UpdateProfileRequest;
import com.gymly.data.model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface ProfileApiService {

    @GET("profile")
    Call<ApiResponse<UserProfile>> getProfile();

    @PUT("profile")
    Call<ApiResponse<UserProfile>> updateProfile(@Body UpdateProfileRequest request);
}
