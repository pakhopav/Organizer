package com.example.pdaorganizer.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.User

class Login : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var errorMessage: TextView
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        skipLoginTask()

        initViews()

        dbHelper = DbHelper(this)
    }

    fun initViews(){
        usernameInput = findViewById(R.id.loginUsernameInput)
        passwordInput = findViewById(R.id.loginPasswordInput)
        errorMessage = findViewById(R.id.errorMessage)
        errorMessage.setVisibility(View.GONE)
    }

    fun validate(username: String, password: String) : User?{
        return dbHelper.returnUserIfExist(username, password)
    }

    fun login(view: View){
        val user = validate(usernameInput.text.toString(), passwordInput.text.toString())
        if(user != null){
            val sp = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putInt(DbHelper.USER_ID, user.id)
            editor.apply()
            startActivity(Intent(this, Overview::class.java))
        }else{
            showErrorMessage()
        }
    }

    fun showErrorMessage(){
        errorMessage.setVisibility(View.VISIBLE)
    }

    fun goToReg(view: View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }


    fun skipLoginTask(){
        val userId = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1)
        if(userId != -1) startActivity(Intent(this, Overview::class.java))
    }
}