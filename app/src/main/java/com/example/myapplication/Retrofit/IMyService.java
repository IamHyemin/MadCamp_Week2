package com.example.myapplication.Retrofit;

import java.util.ArrayList;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IMyService {
    //Get all Users
    @GET("/api/users")
    Call<ArrayList<User>> getAllUser();

    //Get Single User By Email
    @GET("/api/users/{user_email}")
    Call<User> getUser(@Path("user_email") String user_email);

    // CREATE USER
    @POST("/api/users")
    Call<User> createUser(@Body User user);

    // UPDATE THE USER
    @PUT("/api/users/{user_email}")
    Call<User> updateUser(@Path("user_email") String user_email, @Body User user);

    // UPDATE THE User Friend
    @PUT("/api/users/friend/{user_email}/{friend_email}")
    Call<User> updateUserFriend(@Path("user_email") String user_email, @Path("friend_email") String friend_email);

    // UPLOAD (IMAGE) FILE
    @Multipart
    @POST("/api/files/upload")
    Call<MyImage> uploadFile(@Part("imageFile") MultipartBody.Part filePart,
                             @Part("title") RequestBody title,
                             @Part("description") RequestBody description);

    // DELETE SINGLE USER BY EMAIL
    @DELETE("/api/users/{user_email}")
    Call<User> deleteUser(@Path("user_email") String user_email);

    //    @POST("register")
//    @FormUrlEncoded
//    Observable<String> registerUser(@Field("email") String email,
//                                    @Field("name") String name,
//                                    @Field("phoneNum") String phoneNum,
//                                    @Field("password") String password);
//    @POST("login")
//    @FormUrlEncoded
//    Observable<String> loginUser(@Field("email") String email,
//                                    @Field("password") String password);

}
