package com.ydhnwb.resepmau_mvvm.webservices

import com.ydhnwb.resepmau_mvvm.utilities.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit : Retrofit? = null

        private var opt = OkHttpClient.Builder().apply {
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient() : Retrofit {
            return if (retrofit != null){
                retrofit!!
            }else{
                retrofit = Retrofit.Builder().apply {
                    client(opt)
                    baseUrl(Constant.API_ENDPOINT)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            }
        }

        fun instance() : ApiService = getClient().create(ApiService::class.java)
    }
}