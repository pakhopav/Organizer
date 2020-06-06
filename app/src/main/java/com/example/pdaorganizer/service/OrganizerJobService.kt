package com.example.pdaorganizer.service

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pdaorganizer.App
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.helpers.DateHelper
import com.example.pdaorganizer.model.Issue
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

class OrganizerJobService : JobService() {
    private var jobCancelled = false

    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var dbHelper: DbHelper
    override fun onStartJob(params: JobParameters?): Boolean {
        notificationManager = NotificationManagerCompat.from(this)
        dbHelper = DbHelper(this)
        Log.d("OrganizerJobService", "Job Started")
        doBackGroundWork(params)
        return true
    }
    fun doBackGroundWork(params : JobParameters?){
        Thread(Runnable {
            val dateHelper = DateHelper()
            val list = dbHelper.getAllActiveIssuesOfUser(getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(DbHelper.USER_ID, -1))
            val nList = list.stream()
                .filter { it.deadline != "" }
                .filter { dateHelper.getDifferenceFromDateToToday(SimpleDateFormat.getDateInstance().parse(it.deadline)) == 0 }
                .collect(Collectors.toList())
            if(nList.size != 0){
                nList.forEach { sendOnChannel(it) }
            }

            jobFinished(params, false)
        }).start()
    }
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("OrganizerJobService", "Job Stopped")
        jobCancelled = true
        return true
    }



    fun sendOnChannel(issue: Issue){
        val notification = NotificationCompat.Builder(this, App.const.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_calendar_today)
            .setContentTitle("Issue expire today")
            .setContentText("${issue.name}, importance ${issue.importance}")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()
        notificationManager.notify(1, notification)
    }


}