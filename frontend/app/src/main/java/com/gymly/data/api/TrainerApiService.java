package com.gymly.data.api;

import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.BookPtRequest;
import com.gymly.data.model.PtBookingData;
import com.gymly.data.model.Trainer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TrainerApiService {

    @GET("trainers")
    Call<ApiResponse<List<Trainer>>> getTrainers();

    @POST("trainers/book")
    Call<ApiResponse<PtBookingData>> bookSession(@Body BookPtRequest request);
}
