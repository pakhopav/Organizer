package com.example.pdaorganizer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.User

class Register : AppCompatActivity() {

    private lateinit var usernameInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var passwordInputRepeat : EditText
    private lateinit var registerErrorMessage : TextView
    private lateinit var registerButton : Button
    private lateinit var registerEptyErrorMessage : TextView

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
        passwordInputRepeat = findViewById(R.id.passwordInputRepeat)
        registerButton = findViewById(R.id.registerButton)
        registerErrorMessage = findViewById(R.id.textView22)
        registerEptyErrorMessage = findViewById(R.id.registerErrorUsername)
        registerErrorMessage.visibility = View.GONE
        val i = 0
    }


    fun createUser(){

        if(validate()){
            val newUser = User(name = usernameInput.text.toString().trim(),
                password = passwordInput.text.toString().trim())
            dbHelper.addUser(newUser)
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }else{
            registerErrorMessage.visibility = View.VISIBLE
        }

    }

    fun userCreationButtonOnClick(view: View){
        if(usernameInput.text.toString().equals("")){
            registerEptyErrorMessage.visibility = View.VISIBLE
        }else{
            registerEptyErrorMessage.visibility = View.GONE
            createUser()
        }

    }

    fun validate(): Boolean{
        return passwordInputRepeat.text.toString() == passwordInput.text.toString()
    }

}