package com.example.quizappdiploma.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("User-Agent", "LearningNow/1.0 (Android; educational app)")
                    .build()
            )
        }
        .build()

    val openTdb: OpenTdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenTdbApiService::class.java)
    }

    val wikipedia: WikipediaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikipediaApiService::class.java)
    }
}
