package com.example.quizappdiploma.entities

data class Lecturer(
    val name: String,
    val password : String,
    val email : String,
    val isAdmin : Boolean,
    val isLecturer : Boolean,
    val isStudent : Boolean
)