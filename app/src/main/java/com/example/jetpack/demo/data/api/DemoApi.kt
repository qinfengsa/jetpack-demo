package com.example.jetpack.demo.data.api

import com.example.jetpack.demo.data.DemoConstant
import com.example.jetpack.demo.data.api.interceptor.SignInterceptor
import com.example.jetpack.demo.data.api.model.DemoResult
import com.example.jetpack.demo.data.api.model.ImageData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface DemoApi {

    @GET("/images/page")
    suspend fun getImagePage(@Query("page") page: Int = 1): DemoResult<ImageData>

    companion object {

        operator fun invoke(): DemoApi {


            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(SignInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(DemoConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DemoApi::class.java)
        }
    }
}
