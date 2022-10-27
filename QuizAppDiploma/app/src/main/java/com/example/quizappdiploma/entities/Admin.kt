package com.example.quizappdiploma.entities

data class Admin(
    val username : String,
    val password : String,
    val email : String,
    val isAdmin : Boolean,
    val isLecturer : Boolean,
    val isStudent : Boolean
)
