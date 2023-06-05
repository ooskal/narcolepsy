package com.example.narcolepsyproject.db;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonPlaceHolderApi {

    @GET("contact/")
    Call<List<ContactPost>> getContactPosts();

    @Multipart
    @POST("contact/")
    Call<List<ContactPost>> postContact(
            @Part("name")RequestBody param1,
            @Part("phone_number") RequestBody param2
            );

    @GET("sleep/")
    Call<List<Sleep>> getSleepData();



}
