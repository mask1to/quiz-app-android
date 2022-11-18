package com.example.quizappdiploma.entities

data class Student(
    val username : String,
    val password : String,
    val email : String,
    val isAdmin : Int,
    val isLecturer : Int,
    val isStudent : Int
)