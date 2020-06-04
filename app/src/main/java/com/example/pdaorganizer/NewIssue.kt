package com.example.pdaorganizer

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.pdaorganizer.adapter.DatePickFragment
import com.example.pdaorganizer.adapter.IssueCategorySpinnerAdapter
import com.example.pdaorganizer.adapter.IssueImportanceSpinnerAdapter
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.Issue
import com.example.pdaorganizer.model.IssueCategory
import com.example.pdaorganizer.model.IssueImportance
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewIssue : AppCompatActivity() , DatePickerDialog.OnDateSetListener {
    private lateinit var bottomNavigationView :BottomNavigationView

    private lateinit var nameInput : EditText
    private lateinit var descriptionInput : EditText
    private lateinit var deadlineInput : EditText
    private lateinit var button: Button
    private lateinit var deadlineButton: Button


    private lateinit var dbHelper: DbHelper
    private lateinit var categorySpinner: Spinner
    private lateinit var categoryArrayList: ArrayList<IssueCategory>
    private lateinit var categorySpinnerAdapter : IssueCategorySpinnerAdapter
    private lateinit var importanceSpinner: Spinner
    private lateinit var importanceSpinnerAdapter : IssueImportanceSpinnerAdapter
    private var deadlineDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_issue)

        initNavBar()

        initViews()

        initObjects()

        val i = 8

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
        descriptionInput = findViewById(R.id.issueDescription)
        button = findViewById(R.id.issueCreate)
        categorySpinner = findViewById(R.id.issueCategorySpinner)
        importanceSpinner = findViewById(R.id.issueImportanceSpinner)
        deadlineButton = findViewById(R.id.deadlineButton)
    }

    fun initObjects(){
        dbHelper = DbHelper(this)

        categoryArrayList = ArrayList()
        categoryArrayList.add(IssueCategory(R.drawable.ic_no_category, "No category"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_money, "Debt"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_school, "School"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_work, "Work"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_shop, "Shop"))

        categorySpinnerAdapter = IssueCategorySpinnerAdapter(this, categoryArrayList)
        categorySpinner.adapter = categorySpinnerAdapter
        categorySpinner.setVisibility(View.VISIBLE)

        val green = ContextCompat.getColor(this, R.color.colorImportanceL)
        val yellow = ContextCompat.getColor(this, R.color.colorImportanceM)
        val red = ContextCompat.getColor(this, R.color.colorImportanceH)
        val array = arrayListOf(IssueImportance(green, "Low importance"),IssueImportance(yellow, "Mid importance"),IssueImportance(red, "High importance"))
        importanceSpinnerAdapter = IssueImportanceSpinnerAdapter(this, array)
        importanceSpinner.adapter = importanceSpinnerAdapter
        importanceSpinner.setVisibility(View.VISIBLE)
    }

    fun createIssue(){
        val selectedCategoryObj =  categorySpinner.selectedItem as IssueCategory
        val selectedCategory = selectedCategoryObj.name
        val selectedImportanceObj = importanceSpinner.selectedItem as IssueImportance
        val selectedImportance = selectedImportanceObj.name.split(" ")[0]

        val sh = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE)
        val creator = sh.getInt(DbHelper.USER_ID, -1)
        val newIssue = Issue(name = nameInput.text.toString().trim(),
                            user = creator,
                            category = selectedCategory,
                            description =  descriptionInput.text.toString().trim(),
                            importance = selectedImportance,
                            deadline = deadlineDate)
        dbHelper.addIssue(newIssue)
    }


    fun openDialog(view: View){
        val dialog = DatePickFragment()
        dialog.show(supportFragmentManager, "date picker")
    }
    fun issueCreationButtonOnClick(view: View){
        createIssue()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        deadlineDate = DateFormat.getDateInstance().format(c.time)
    }
}