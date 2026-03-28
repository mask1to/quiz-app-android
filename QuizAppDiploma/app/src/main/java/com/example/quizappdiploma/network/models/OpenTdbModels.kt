package com.example.quizappdiploma.network.models

import com.google.gson.annotations.SerializedName

data class OpenTdbCategoriesResponse(
    @SerializedName("trivia_categories") val categories: List<OpenTdbCategory>
)

data class OpenTdbCategory(
    val id: Int,
    val name: String
) {
    override fun toString() = name
}

data class OpenTdbQuestionsResponse(
    @SerializedName("response_code") val responseCode: Int,
    val results: List<OpenTdbQuestion>
)

data class OpenTdbQuestion(
    val category: String,
    val difficulty: String,
    val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>
)
