package com.example.quizappdiploma.database

object DatabaseConstants
{
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "learningnow"
    const val TBL_USERS = "tbl_users"
    const val TBL_COURSE = "tbl_course"
    const val TBL_LECTURE = "tbl_lecture"
    const val TBL_QUIZ = "tbl_quiz"
    const val TBL_QUIZ_QUESTIONS = "tbl_quiz_questions"
    const val TBL_QUIZ_QUESTION_ANSWERS = "tbl_quiz_question_answers"

    /**
     * Table users
     */
    const val KEY_ID_STUDENT = "id "
    const val KEY_USERNAME = "username "
    const val KEY_EMAIL = "email "
    const val KEY_PASSWORD = "password "
    const val KEY_STUDENT = "isStudent "
    const val KEY_LECTURER = "isLecturer "
    const val KEY_ADMIN = "isAdmin "

    /**
     * Table course
     */
    const val KEY_ID_COURSE = "id "
    const val KEY_NAME_COURSE = "course_name "
    const val KEY_IMAGE_ID_COURSE = "course_image_id "

    /**
     * Table lecture
     */
    const val KEY_ID_LECTURE = "id "
    const val KEY_NAME_LECTURE = "lecture_name "
    const val KEY_DESC_LECTURE = "content_description "
    const val KEY_IMAGE_ID_LECTURE = "lecture_image_id "

    /**
     * Table quiz
     */
    const val KEY_ID_QUIZ = "quiz_id "
    const val QUIZ_NAME = "quiz_name "
    const val QUIZ_ACTIVE = "is_active "
    const val QUIZ_DIFFICULTY = "quiz_level"

    /**
     * Table quiz questions
     */
    const val KEY_ID_QUESTION = "question_id "
    const val QUESTION_NAME = "question_name "
    const val QUESTION_POINTS = "question_points "
    const val QUESTION_DIFFICULTY = "question_level "
    const val QUESTION_MULTI_CHOICE = "question_multi_choice"

    /**
     * Table answers
     */
    const val KEY_ID_ANSWER = "quiz_question_answer_id"
    const val ANSWER = "quiz_question_answer"
    const val ANSWER_CORRECT = "is_answer_correct"
}