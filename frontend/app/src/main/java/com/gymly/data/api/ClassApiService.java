package com.gymly.data.api;

import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.BookClassRequest;
import com.gymly.data.model.ClassBookingData;
import com.gymly.data.model.GymClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ClassApiService {

    @GET("classes")
    Call<ApiResponse<List<GymClass>>> getClasses();

    @POST("classes/book")
    Call<ApiResponse<ClassBookingData>> bookClass(@Body BookClassRequest request);
}
