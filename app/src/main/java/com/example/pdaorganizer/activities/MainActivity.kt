package com.example.pdaorganizer.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
//import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar;
import com.example.pdaorganizer.helpers.DateHelper

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var button : Button
    private lateinit var usernameField : TextView
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DbHelper(this)

        initNavBar()

        initViews()
    }


    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(0).setChecked(true)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.overview -> {
                    startActivity(Intent(applicationContext, Overview::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.newIssueExpireDateInput -> {
                    startActivity(Intent(applicationContext, NewIssue::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.statistics -> {
                    startActivity(Intent(applicationContext, Statistics::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }


    fun initViews(){
        usernameField = findViewById(R.id.usernameContainer)
        val username = dbHelper.getUserById(getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1))
        usernameField.setText(username?.name)
    }

    fun logOut(view: View){
        val sp = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sp.edit()
        val i = -1
        editor.putInt(DbHelper.USER_ID, i)
        editor.apply()
        startActivity(Intent(this, Login::class.java))
    }
}