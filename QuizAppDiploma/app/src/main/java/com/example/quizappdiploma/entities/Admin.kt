package com.example.quizappdiploma.entities

data class Admin(
    val id : Int,
    val username : String,
    val email : String,
    val password : String,
    val isAdmin : Int,
    val isLecturer : Int,
    val isStudent : Int
)
