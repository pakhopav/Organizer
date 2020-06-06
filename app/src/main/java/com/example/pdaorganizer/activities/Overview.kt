package com.example.pdaorganizer.activities

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.App
import com.example.pdaorganizer.R
import com.example.pdaorganizer.adapter.RecycleViewAdapter
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.helpers.DateHelper
import com.example.pdaorganizer.model.Issue
import com.example.pdaorganizer.model.User
import com.example.pdaorganizer.service.OrganizerJobService
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.stream.Collectors

class Overview : AppCompatActivity() {
    private lateinit var bottomNavigationView :BottomNavigationView
    private lateinit var recyclerView : RecyclerView

    private var currentImpFilter  = 0
    private var currentCatFilter  = 0
    private lateinit var dbHelper: DbHelper
    private lateinit var issuesArray: ArrayList<Issue>
    lateinit var notificationManager : NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        initNavBar()
        initObjects()

        startJob()
        notificationManager = NotificationManagerCompat.from(this)
        recyclerView = findViewById(R.id.issuesRecycler)
        val adapter = RecycleViewAdapter(this, sortDown(issuesArray))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(0).setChecked(true)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
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
        issuesArray = ArrayList<Issue>()
        issuesArray.addAll(dbHelper.getAllIssuesForUser(userId).stream().filter { it.active == "t" || it.active == "e" }.collect(Collectors.toList()))
        val i  = 0
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun sortDown(arr :ArrayList<Issue>):ArrayList<Issue>{
        val dateHelper = DateHelper()
        val nArray = ArrayList<Issue>()
        nArray.addAll(arr.stream()
            .filter { it.deadline != "" }
            .sorted{ o1, o2 ->  dateHelper.getDifferenceFromDateToToday(SimpleDateFormat.getDateInstance().parse(o1.deadline)).compareTo(dateHelper.getDifferenceFromDateToToday(SimpleDateFormat.getDateInstance().parse(o2.deadline))) }
            .collect(Collectors.toList()))
        nArray.addAll(arr.stream().filter { it.deadline == "" }.collect(Collectors.toList()))
        return nArray
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllLowImp(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.importance == "Low" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllMidImp(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.importance == "Mid" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllHighImp(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.importance == "High" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllAllImp(): ArrayList<Issue>{
        return issuesArray
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllNothCat(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.category == "No category" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllSchoolCat(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.category == "School" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllDebtCat(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.category == "Debt" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllWorkCat(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.category == "Work" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllShopCat(): ArrayList<Issue>{
        val nArray = java.util.ArrayList<Issue>()
        nArray.addAll(issuesArray.stream().filter { it.category == "Shop" }.collect(Collectors.toList()))
        return nArray
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllAllCat(): ArrayList<Issue>{
        return issuesArray
    }




    @RequiresApi(Build.VERSION_CODES.N)
    fun filterCategoryIssues(){
        var impSorted = ArrayList<Issue>()
        var nArray = ArrayList<Issue>()
        when(currentImpFilter){
            0 -> impSorted = getAllAllImp()
            1 -> impSorted = getAllLowImp()
            2 -> impSorted = getAllMidImp()
            3 -> impSorted = getAllHighImp()
        }
        when(currentCatFilter){
            0 -> nArray = impSorted
            1 -> nArray.addAll(impSorted.stream().filter { it.category == "No category" }.collect(Collectors.toList()))
            2 -> nArray.addAll(impSorted.stream().filter { it.category == "Debt" }.collect(Collectors.toList()))
            3 -> nArray.addAll(impSorted.stream().filter { it.category == "School" }.collect(Collectors.toList()))
            4 -> nArray.addAll(impSorted.stream().filter { it.category == "Work" }.collect(Collectors.toList()))
            5 -> nArray.addAll(impSorted.stream().filter { it.category == "Shop" }.collect(Collectors.toList()))
        }
        val adapter = RecycleViewAdapter(this, sortDown(nArray))
        recyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun filterImportanceIssues(){
        var catSorted = ArrayList<Issue>()
        var nArray = ArrayList<Issue>()
        when(currentCatFilter){
            0 -> catSorted = getAllAllCat()
            1 -> catSorted = getAllNothCat()
            2 -> catSorted = getAllDebtCat()
            3 -> catSorted = getAllSchoolCat()
            4 -> catSorted = getAllWorkCat()
            5 -> catSorted = getAllShopCat()
        }
        when(currentImpFilter){
            0 -> nArray = catSorted
            1 -> nArray.addAll(catSorted.stream().filter { it.importance == "Low" }.collect(Collectors.toList()))
            2 -> nArray.addAll(catSorted.stream().filter { it.importance == "Mid" }.collect(Collectors.toList()))
            3 -> nArray.addAll(catSorted.stream().filter { it.importance == "High" }.collect(Collectors.toList()))

        }
        val adapter = RecycleViewAdapter(this, sortDown(nArray))
        recyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun openImportanceFilter(view: View){
        val popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.filterImpItem1 -> {
                    if(currentImpFilter != 0) {
                        currentImpFilter = 0
                        filterImportanceIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterImpItem2 -> {
                    if(currentImpFilter != 1) {
                        currentImpFilter = 1
                        filterImportanceIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterImpItem3 -> {
                    if(currentImpFilter != 2) {
                        currentImpFilter = 2
                        filterImportanceIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterImpItem4 -> {
                    if(currentImpFilter != 3) {
                        currentImpFilter = 3
                        filterImportanceIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                else  -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }
        popup.inflate(R.menu.popup_filter_importance)
        popup.show()

    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun openCategoryFilter(view: View){
        val popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.filterCatItem1 -> {
                    if(currentCatFilter != 0) {
                        currentCatFilter = 0
                        filterCategoryIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterCatItem2  -> {
                    if(currentCatFilter != 1) {
                        currentCatFilter = 1
                        filterCategoryIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterCatItem3 -> {
                    if(currentCatFilter != 2) {
                        currentCatFilter = 2
                        filterCategoryIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterCatItem4 -> {
                    if(currentCatFilter != 3) {
                        currentCatFilter = 3
                        filterCategoryIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterCatItem5 -> {
                    if(currentCatFilter != 4) {
                        currentCatFilter = 4
                        filterCategoryIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.filterCatItem6 -> {
                    if(currentCatFilter != 5) {
                        currentCatFilter = 5
                        filterCategoryIssues()
                    }
                    return@setOnMenuItemClickListener true
                }
                else  -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }
        popup.inflate(R.menu.popup_filter_category)
        popup.show()
    }




    fun startJob(){
        val component = ComponentName(this, OrganizerJobService::class.java)
        val jobInfo = JobInfo.Builder(123, component)
            .setPersisted(true)
            .setPeriodic(1000*60*15)
            .build()
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE)as JobScheduler
        val i = scheduler.schedule(jobInfo)
        val d = i
    }

    fun stopJob(view: View){
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE)as JobScheduler
        scheduler.cancel(123)
    }

}