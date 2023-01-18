package com.example.quizappdiploma.database.quizzes.questions

import com.example.quizappdiploma.R

object ConstantsBeta
{
    fun getFirstFiveQuestions():ArrayList<QuizQuestionModel>{
        val questionList = ArrayList<QuizQuestionModel>()

        val firstQuestion = QuizQuestionModel(
            1, "How are you?", R.drawable.icon, 2,1, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(firstQuestion)

        val secondQuestion = QuizQuestionModel(
            1, "How are you2?", R.drawable.icon, 2,1, "Good", "Bad", "Neutral", "None of the above",2
        )

        questionList.add(secondQuestion)

        val thirdQuestion = QuizQuestionModel(
            1, "How are you3?", R.drawable.icon, 2,1, "Good", "Bad", "Neutral", "None of the above",3
        )

        questionList.add(thirdQuestion)

        val fourthQuestion = QuizQuestionModel(
            1, "How are you4?", R.drawable.icon, 2,2, "Good", "Bad", "Neutral", "None of the above",4
        )

        questionList.add(fourthQuestion)

        val fifthQuestion = QuizQuestionModel(
            1, "How are you5?", R.drawable.icon, 2,2, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(fifthQuestion)

        return questionList
    }

}