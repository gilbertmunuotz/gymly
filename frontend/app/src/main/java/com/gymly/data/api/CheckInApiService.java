package com.gymly.data.api;

import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.AttendanceRecord;
import com.gymly.data.model.CheckInRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CheckInApiService {

    @GET("check-in/history")
    Call<ApiResponse<List<AttendanceRecord>>> getHistory();

    @POST("check-in")
    Call<ApiResponse<AttendanceRecord>> checkIn(@Body CheckInRequest request);
}
