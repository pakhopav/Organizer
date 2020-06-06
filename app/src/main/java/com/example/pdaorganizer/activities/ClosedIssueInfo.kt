package com.example.pdaorganizer.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.helpers.StorageHelper
import com.example.pdaorganizer.model.Issue
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClosedIssueInfo : AppCompatActivity() {
    lateinit var  myIssue : Issue
    lateinit var dbHelper: DbHelper
    lateinit var storageHelper: StorageHelper
    private lateinit var bottomNavigationView : BottomNavigationView

    lateinit var issueName: TextView
    lateinit var issueImportance: TextView
    lateinit var issueCategory: TextView
    lateinit var issueDescription: TextView
    lateinit var issueDeadline: TextView
    lateinit var imageLabel: TextView
    lateinit var image: ImageView
    lateinit var bigImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closed_issue_info)

        dbHelper = DbHelper(this)
        storageHelper = StorageHelper(this)



        val issueId = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(
            DbHelper.ISSUE_ID ,-1)
        val issue = dbHelper.getIssueById(issueId)
        if(issue != null){
            myIssue = issue
        }

        initNavBar()
        initViews()
    }


    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(2).setChecked(true)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.overview -> {
                    startActivity(Intent(applicationContext, Overview::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.newIssueExpireDateInput -> true

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
        issueName = findViewById(R.id.infoNameC)
        issueImportance = findViewById(R.id.infoImportanceC)
        issueCategory = findViewById(R.id.infoCategoryC)
        issueDescription = findViewById(R.id.infoDescriptionC)
        issueDeadline = findViewById(R.id.infoDeadlineC)
        imageLabel =findViewById(R.id.infoImageLabelC)
        image = findViewById(R.id.infoIssueImageC)
        bigImage =findViewById(R.id.infoIssueBigImageC)


        issueName.setText(myIssue.name)
        issueImportance.setText(myIssue.importance)
        when(myIssue.importance){
            "Low" -> issueImportance.setTextColor(
                ContextCompat.getColor(this,
                R.color.colorImportanceL
            ))
            "Mid" -> issueImportance.setTextColor(
                ContextCompat.getColor(this,
                R.color.colorImportanceM
            ))
            "High" -> issueImportance.setTextColor(
                ContextCompat.getColor(this,
                R.color.colorImportanceH
            ))
        }
        issueCategory.setText(myIssue.category)
        issueDescription.setText(myIssue.description)
        if(!myIssue.deadline.equals("")) issueDeadline.setText(myIssue.deadline)
        else issueDeadline.setText("-")

        if(myIssue.photoPath.equals("")){
            image.visibility= View.GONE
            imageLabel.setText("No photo")
        }else{
            image.visibility= View.VISIBLE
            image.setImageBitmap(storageHelper.loadImageFromStorage(myIssue.photoPath))
            bigImage.setImageBitmap(storageHelper.loadImageFromStorage(myIssue.photoPath))
        }
    }




    fun openBigImage(view: View){
        bigImage.visibility = View.VISIBLE
    }
    fun hideBigImage(view: View){
        bigImage.visibility = View.GONE
    }
}