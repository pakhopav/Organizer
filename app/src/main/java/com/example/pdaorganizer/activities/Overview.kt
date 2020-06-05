package com.example.pdaorganizer.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.adapter.RecycleViewAdapter
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.Issue
import com.google.android.material.bottomnavigation.BottomNavigationView

class Overview : AppCompatActivity() {
    private lateinit var bottomNavigationView :BottomNavigationView
    private lateinit var recyclerView : RecyclerView


    private lateinit var dbHelper: DbHelper
    private lateinit var issuesArray: ArrayList<Issue>
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        initNavBar()

        initObjects()

        recyclerView = findViewById(R.id.issuesRecycler)
        val adapter = RecycleViewAdapter(this, issuesArray)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(1).setChecked(true)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.overview -> return@OnNavigationItemSelectedListener true
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

    fun initObjects(){
        dbHelper = DbHelper(this)
        val userId = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1)
        issuesArray = ArrayList(dbHelper.getAllActiveIssuesOfUser(userId))
        val i  = 0
    }
}