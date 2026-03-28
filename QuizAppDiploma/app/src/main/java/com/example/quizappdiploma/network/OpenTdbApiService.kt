package com.example.quizappdiploma.network

import com.example.quizappdiploma.network.models.OpenTdbCategoriesResponse
import com.example.quizappdiploma.network.models.OpenTdbQuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTdbApiService {

    @GET("api_category.php")
    suspend fun getCategories(): OpenTdbCategoriesResponse

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String = "multiple"
    ): OpenTdbQuestionsResponse
}
