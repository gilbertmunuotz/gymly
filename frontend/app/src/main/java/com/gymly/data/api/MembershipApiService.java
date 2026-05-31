package com.gymly.data.api;

import com.gymly.data.model.ApiResponse;
import com.gymly.data.model.MembershipData;
import com.gymly.data.model.MembershipPlan;
import com.gymly.data.model.SubscribeRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MembershipApiService {

    @GET("memberships/plans")
    Call<ApiResponse<List<MembershipPlan>>> getPlans();

    @GET("memberships/active")
    Call<ApiResponse<MembershipData>> getActiveMembership();

    @POST("memberships/subscribe")
    Call<ApiResponse<MembershipData>> subscribe(@Body SubscribeRequest request);
}
