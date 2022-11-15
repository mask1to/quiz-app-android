

# LearnMeNow repository 

The repository is for android application "**LearnMeNow**", which is being developed for **diploma thesis purposes**.

Main target of the app is to learn you something new via course, in which you will be able to enroll in and then take the quiz :)


# Main changes

#### 08/10/2022

 - SQLiteDatabase imported and implemented partially
 - Code split into 3 main packages - database, entities, fragments
	 - Entities -> Student, Lecturer, Admin
	 - Database -> DatabaseHandler
	 - Fragments -> RegistrationFragment, WelcomeFragment

#### 10/10/2022
- Registration and Welcome UI updated by material.io
- Navigation between fragments 
	- from login action based on who logged in (admin / lecturer / student)
- SplashScreen with animation

#### 01/11/2022
- Adapters for Course and Lecture added
- RecyclerView implemented for courses and lectures
- Course and Lecture are from now on data classes as Admin, Student, Lecturer


# Important files/packages

## Entities

 - Admin.kt
 - Lecturer.kt
 - Student.kt
 - Course.kt
 - Lecture.kt

## Fragments

 - WelcomeFragment.kt
 - RegistrationFragment.kt
 - AdminFragment.kt
 - StudentFragment.kt
 - LecturerFragment.kt
 - BiologyCourse.kt, BiologyContent.kt, BiologyQuiz.kt

# Docs reference
**Fragments**</br>
https://developer.android.com/guide/fragments </br></br>

**SQLiteDatabase**</br>
https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase </br></br>

**Splash screens**</br>
https://developer.android.com/develop/ui/views/launch/splash-screen </br></br>



# Have fun :)
![enter image description here](https://i.imgur.com/BLre990.jpg)