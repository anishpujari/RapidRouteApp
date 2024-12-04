package com.example.rapidroute;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    // Endpoint for Signup
    @POST("auth/sign")
    Call<Void> signUp(@Body User user);

    // Endpoint for Login
    @POST("auth/login")
    Call<UserResponse> login(@Body User user);
}
