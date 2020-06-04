package com.example.pdaorganizer

import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.Issue
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewIssue : AppCompatActivity() {
    private lateinit var bottomNavigationView :BottomNavigationView

    private lateinit var nameInput : EditText
    private lateinit var categoryInput : EditText
    private lateinit var descriptionInput : EditText
    private lateinit var importanceInput : EditText
    private lateinit var deadlineInput : EditText
    private lateinit var button: Button

    private lateinit var dbHelper: DbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_issue)

        initNavBar()

        initViews()

        dbHelper = DbHelper(this)

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
                R.id.newIssue -> true

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
        nameInput = findViewById(R.id.issueName)
        categoryInput = findViewById(R.id.issueCategory)
        descriptionInput = findViewById(R.id.issueDescription)
        importanceInput = findViewById(R.id.issueImportance)
        deadlineInput = findViewById(R.id.issueDeadline)
        button = findViewById(R.id.issueCreate)
    }


    fun createIssue(){
        val newIssue = Issue(name = nameInput.text.toString().trim(),
                            category = categoryInput.text.toString().trim(),
                            description =  descriptionInput.text.toString().trim(),
                            importance = importanceInput.text.toString().trim(),
                            deadline = deadlineInput.text.toString().trim())
        dbHelper.addIssue(newIssue)
    }
    fun createIssue2(){

        dbHelper.addIssue2(nameInput.text.toString().trim())
    }


    fun issueCreationButtonOnClick(view: View){
        createIssue()
//        createIssue2()
    }
}