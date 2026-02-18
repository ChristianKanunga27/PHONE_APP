package com.example.phone_shop_app;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("/addPhone")
    Call<Void> addPhone(
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price
    );
}
