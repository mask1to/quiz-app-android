package com.example.quizappdiploma.database.quizzes.questions

import com.example.quizappdiploma.R
import com.example.quizappdiploma.database.MyDatabase
object ConstantsBeta
{
    /*fun getFirstFiveQuestions():ArrayList<QuizQuestionModel>{
        val questionList = ArrayList<QuizQuestionModel>()

        val firstQuestion = QuizQuestionModel(
            1, "How are you?", "random path", 2,1, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(firstQuestion)

        val secondQuestion = QuizQuestionModel(
            2, "How are you2?", "random path", 2,1, "Good", "Bad", "Neutral", "None of the above",2
        )

        questionList.add(secondQuestion)

        val thirdQuestion = QuizQuestionModel(
            3, "How are you3?", "random path", 2,1, "Good", "Bad", "Neutral", "None of the above",3
        )

        questionList.add(thirdQuestion)

        val fourthQuestion = QuizQuestionModel(
            4, "How are you4?", "random path", 2,2, "Good", "Bad", "Neutral", "None of the above",4
        )

        questionList.add(fourthQuestion)

        val fifthQuestion = QuizQuestionModel(
            5, "How are you5?", "random path", 2,2, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(fifthQuestion)

        val sixthQuestion = QuizQuestionModel(
            6, "How are you6?","random path", 2,2, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(sixthQuestion)

        val seventhQuestion = QuizQuestionModel(
            7, "How are you7?", "random path", 2,2, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(seventhQuestion)

        val eighthQuestion = QuizQuestionModel(
            8, "How are you8?", "random path", 2,2, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(eighthQuestion)

        val ninthQuestion = QuizQuestionModel(
            9, "How are you9?", "random path", 2,2, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(ninthQuestion)

        val tenthQuestion = QuizQuestionModel(
            10, "How are you10?", "random path", 2,2, "Good", "Bad", "Neutral", "None of the above",1
        )

        questionList.add(tenthQuestion)

        return questionList
    }*/

    fun getFirstFiveQuestions(dao: QuizQuestionDao): ArrayList<QuizQuestionModel> {
        val questionList = ArrayList<QuizQuestionModel>()

        for (i in 1..10) {
            val question = dao.getQuestionById(i)
            question?.let {
                questionList.add(it)
            }
        }

        return questionList
    }

}