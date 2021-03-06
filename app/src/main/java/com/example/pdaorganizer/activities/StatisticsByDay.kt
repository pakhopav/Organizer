package com.example.pdaorganizer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.adapter.DaysRecycleAdapter
import com.example.pdaorganizer.adapter.RecycleViewAdapter
import com.example.pdaorganizer.model.Day
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.reflect.Array

class StatisticsByDay : AppCompatActivity() {
    private lateinit var bottomNavigationView :BottomNavigationView
    private lateinit var todayField:TextView

    private lateinit var recyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_by_day)

        todayField =findViewById(R.id.statisticsByDayLabel)
        todayField.setText(intent.getStringExtra("date"))

        initNavBar()

        val date = intent.getStringExtra("date")
        val array = intent.getIntArrayExtra("array")
        recyclerView = findViewById(R.id.statisticsByDayRecycle)
        val arr = ArrayList<Day>()
        for (i in 0..29){
            if(array[i] != 0) arr.add(Day(i, array[i]))
        }
        val adapter = DaysRecycleAdapter(this, date, arr)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(2).setChecked(true)
//        bottomNavigationView.getMenu().getItem(R.id.statistics).setChecked(true)
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
}