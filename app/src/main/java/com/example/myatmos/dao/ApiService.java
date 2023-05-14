package com.example.myatmos.dao;


import com.example.myatmos.entities.Station;
import com.example.myatmos.entities.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("/api/login?")
    Call<User> performLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/get?")
    Call<List<Station>> getDevices(@Field("email") String email, @Field("password") String password);
}