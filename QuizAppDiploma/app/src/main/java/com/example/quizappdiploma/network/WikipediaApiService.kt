package com.example.quizappdiploma.network

import com.example.quizappdiploma.network.models.WikipediaActionResponse
import com.example.quizappdiploma.network.models.WikipediaSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WikipediaApiService {

    @GET("api/rest_v1/page/summary/{title}")
    suspend fun getPageSummary(
        @Path("title") title: String
    ): WikipediaSummaryResponse

    // Action API — full plain-text extract (up to ~8000 chars)
    @GET("w/api.php")
    suspend fun getFullArticle(
        @Query("action") action: String = "query",
        @Query("prop") prop: String = "extracts",
        @Query("titles") titles: String,
        @Query("format") format: String = "json",
        @Query("explaintext") explaintext: Int = 1,
        @Query("redirects") redirects: Int = 1,
        @Query("exchars") exchars: Int = 8000
    ): WikipediaActionResponse
}
