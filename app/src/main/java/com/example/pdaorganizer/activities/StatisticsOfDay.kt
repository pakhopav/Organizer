package com.example.pdaorganizer.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.adapter.RecycleViewAdapter
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.Issue
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.stream.Collectors

class StatisticsOfDay : AppCompatActivity() {
    private lateinit var bottomNavigationView :BottomNavigationView
    private lateinit var recyclerView : RecyclerView
    private lateinit var date : TextView


    private lateinit var dateFromExtra :String
    private lateinit var dbHelper: DbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_of_day)

        dateFromExtra = intent.getStringExtra("date")

        initNavBar()

        dbHelper = DbHelper(this)

        initViews()

    }

    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(2).setChecked(true)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

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
                R.id.statistics ->  true

            }
            false
        })
    }


    fun initViews(){
        date = findViewById(R.id.statisticsOfDayDate)
        date.setText(dateFromExtra)
        val userId = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1)
        val issuesArray = ArrayList<Issue>()
        val y = dateFromExtra.split(" ")[2]
        val m = dateFromExtra.split(" ")[1].slice(0..2)
        var d = dateFromExtra.split(" ")[0]
        if(d.length == 1)d = "0"+d
        issuesArray.addAll( dbHelper.getAllIssuesForUser(userId).stream().filter { it.active == "f" || it.active == "ex" }

            .filter {
                it.closeDate.split(" ")[1] == m && it.closeDate.split(" ")[2] == d && it.closeDate.split(" ").last() == y
            }
            .collect(Collectors.toList()))
        recyclerView = findViewById(R.id.statOfDayRecycler)
        val adapter = RecycleViewAdapter(this, issuesArray)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}