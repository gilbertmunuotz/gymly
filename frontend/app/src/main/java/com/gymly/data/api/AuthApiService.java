package com.gymly.data.api;

import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.AuthData;
import com.gymly.data.model.LoginRequest;
import com.gymly.data.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

    @POST("auth/login")
    Call<ApiResponse<AuthData>> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<ApiResponse<AuthData>> register(@Body RegisterRequest request);
}
