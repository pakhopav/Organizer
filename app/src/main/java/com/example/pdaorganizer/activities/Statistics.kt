package com.example.pdaorganizer.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.helpers.DateHelper
import com.example.pdaorganizer.model.Issue
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_statistics.*
import java.util.*


class Statistics : AppCompatActivity() {
    private lateinit var bottomNavigationView :BottomNavigationView
    val monthArray = arrayListOf("January","February","March","April","May","June","July","August","September","October","November","December")
    lateinit var dateText: TextView
    private lateinit var dbHelper: DbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        dbHelper = DbHelper(this)


        dateText = findViewById(R.id.dateText)
        val today = Calendar.getInstance().time
        dateText.setText("${monthArray[today.month]} ${1900 + today.year}")


        initNavBar()


    }

    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(3).setChecked(true)
//        bottomNavigationView.getMenu().getItem(R.id.statistics).setChecked(true)
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
    @RequiresApi(Build.VERSION_CODES.N)
    fun showDialog(view: View){
        val pickerDialog = MonthYearPickerDialog()
        pickerDialog.setListener(OnDateSetListener { datePicker, year, month, i2 ->
            dateText.setText("${monthArray[month]} ${year}")
            val n = DateHelper().getdays(year -1900, month-1 ,2).toInt()
            configureGraf(n)

        })
        pickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun configureGraf(n: Int){
        val userId = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1)
        val issues = dbHelper.getAllClosedIssuesOfUser(userId)
        issues.stream().filter { it.closeDate>= n  }.forEach {  }
    }


    class MonthYearPickerDialog() : DialogFragment() {
        private var listener: DatePickerDialog.OnDateSetListener? = null
        fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
            this.listener = listener
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
            // Get the layout inflater
            val inflater: LayoutInflater = getActivity()!!.getLayoutInflater()
            val cal = Calendar.getInstance()
            val dialog: View = inflater.inflate(R.layout.month_year_picker_dialog, null)
            val monthPicker =
                dialog.findViewById<View>(R.id.picker_month) as NumberPicker
            val yearPicker =
                dialog.findViewById<View>(R.id.picker_year) as NumberPicker
            monthPicker.minValue = 1
            monthPicker.maxValue = 12
            monthPicker.value = cal[Calendar.MONTH] + 1
            val year = cal[Calendar.YEAR]
            yearPicker.minValue = 2000
            yearPicker.maxValue = year
            yearPicker.value = year
            builder.setView(dialog) // Add action buttons
                .setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener!!.onDateSet(
                            null,
                            yearPicker.value,
                            monthPicker.value,
                            0)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        this@MonthYearPickerDialog.getDialog()!!.cancel()
                    })
            return builder.create()
        }

    }

}