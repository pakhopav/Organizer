package com.example.pdaorganizer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.User

class Register : AppCompatActivity() {

    private lateinit var usernameInput : EditText
    private lateinit var passwordInput : EditText

    private lateinit var registerButton : Button

    private lateinit var dbHelper: DbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        dbHelper = DbHelper(this)
        initViews()
    }


    fun initViews(){
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        registerButton = findViewById(R.id.registerButton)
    }


    fun createUser(){
        val newUser = User(name = usernameInput.text.toString().trim(),
            password = passwordInput.text.toString().trim())
        dbHelper.addUser(newUser)
    }

    fun userCreationButtonOnClick(view: View){
        createUser()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}