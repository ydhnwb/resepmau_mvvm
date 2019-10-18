package com.ydhnwb.resepmau_mvvm.webservices

import com.google.gson.annotations.SerializedName
import com.ydhnwb.resepmau_mvvm.models.Post
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("api/post")
    fun allPost(@Header("Authorization") token : String) : Call<WrappedListResponse<Post>>


    @FormUrlEncoded
    @POST("api/post")
    fun createPost(@Header("Authorization") token : String, @Field("title") title : String, @Field("content") content : String) : Call<WrappedResponse<Post>>

    @FormUrlEncoded
    @PUT("api/post/{id}")
    fun updatePost(@Path("id") id : String, @Header("Authorization") token : String, @Field("title") title : String, @Field("content") content : String) : Call<WrappedResponse<Post>>

    @FormUrlEncoded
    @DELETE("api/post/{id}")
    fun deletePost(@Header("Authorization") token : String, @Path("id") id : String) : Call<WrappedResponse<Post>>

    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("email") email : String, @Field("password") password : String)

    @FormUrlEncoded
    @POST("api/register")
    fun register(@Field("name") name : String, @Field("email") email : String, @Field("password") password : String)

}


data class WrappedResponse<T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : String,
    @SerializedName("data") var data : T? = null
)
data class WrappedListResponse<T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : String,
    @SerializedName("data") var data : List<T>? = null
)

