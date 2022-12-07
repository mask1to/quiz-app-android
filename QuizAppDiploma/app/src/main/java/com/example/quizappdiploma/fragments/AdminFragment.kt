package com.example.quizappdiploma.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import com.example.quizappdiploma.R

class AdminFragment : Fragment()
{

    private lateinit var theUsername : EditText
    private lateinit var theEmail : EditText
    private lateinit var thePassword : EditText


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //disabled onBackPressed
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }

    fun addStudent(view : View)
    {
        val username = theUsername.text.toString()
        val email = theEmail.text.toString()
        val password = thePassword.text.toString()
        val databaseHandler = DatabaseHandler(this.requireContext())

        if(!username.isEmpty() && !email.isEmpty())
        {
            /*val status = databaseHandler.insertNewStudent(Student(0, username, email, password, 0, 0, 0))

            if(status > -1)
            {
                Toast.makeText(context, "Entry saved", Toast.LENGTH_SHORT).show()
                theUsername.text.clear()
                theEmail.text.clear()
                thePassword.text.clear()
            }
            else
            {
                Toast.makeText(context, "Entry not saved", Toast.LENGTH_SHORT).show()
            }*/
        }

    }
}