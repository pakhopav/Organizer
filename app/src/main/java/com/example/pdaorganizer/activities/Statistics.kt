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
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.helpers.DateHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import java.util.stream.Collectors


class Statistics : AppCompatActivity() {
    private lateinit var bottomNavigationView :BottomNavigationView
    val monthArray = arrayListOf("January","February","March","April","May","June","July","August","September","October","November","December")
    lateinit var dateText: TextView
    lateinit var usernameCont :TextView
    lateinit var totalClosed: TextView
    lateinit var totalExpired :TextView
    lateinit var arrayViews :Array<View>
    private lateinit var dbHelper: DbHelper
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        arrayViews = arrayOf(findViewById<View>(R.id.view2),findViewById<View>(R.id.view3),findViewById<View>(R.id.view4),findViewById<View>(R.id.view5),findViewById<View>(R.id.view6),findViewById<View>(R.id.view7),findViewById<View>(R.id.view8),findViewById<View>(R.id.view9),findViewById<View>(R.id.view10),findViewById<View>(R.id.view11))
        dbHelper = DbHelper(this)



        initViews()

        initNavBar()


    }

    fun initViews(){
        usernameCont = findViewById(R.id.usernameCont)
        val username = dbHelper.getUserById(getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1))
        usernameCont.setText(username?.name)

        totalClosed = findViewById(R.id.totalClosed)
        totalExpired = findViewById(R.id.totalExpired)
        val userId = getSharedPreferences(DbHelper.SHARED_PREFS, MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1)
        val issues = dbHelper.getAllClosedIssuesOfUser(userId)
        val closed = issues.size
        val expired = dbHelper.getAllIssuesForUser(userId).stream().filter{it.active == "e"||it.active == "ex"}.count()
        totalClosed.setText(closed.toString())
        totalExpired.setText(expired.toString())
        dateText = findViewById(R.id.dateText)
        val today = Calendar.getInstance().time
        dateText.setText("${monthArray[today.month]} ${1900 + today.year}")
        val n = DateHelper().getdays(today.year , today.month ,1).toInt()
        configureGraf(n)
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
    fun showDialog(view: View){
        val pickerDialog = MonthYearPickerDialog()
        pickerDialog.setListener(OnDateSetListener { datePicker, year, month, i2 ->
            dateText.setText("${monthArray[month]} ${year}")
            val n = DateHelper().getdays(year -1900, month-1 ,2).toInt()
            configureGraf(n)

        })
        pickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
    }

    fun configureGraf(n: Int){
            val array = arrayOf(0,0,0,0,0,0,0,0,0,0)
            val dh =DateHelper()
            val userId = getSharedPreferences(DbHelper.SHARED_PREFS, MODE_PRIVATE).getInt(DbHelper.USER_ID ,-1)
            val issues = dbHelper.getAllClosedIssuesOfUser(userId)
            if(issues.size ==0)return
            val l = issues.stream().map { dh.getdays(it.closeDate).toInt()}.collect(Collectors.toList())
            val ll= l.filter{ it>= n  }.map { (it-n)/3 }
            val lll = ll   .filter{it < 10}.forEach { array[it]++ }
            for(i in 0..9 ){
                var newHeight =  5 + array[i]*19
                if(newHeight == 5)continue
                if(newHeight >= 195) newHeight =195
                arrayViews[i].layoutParams.height =  newHeight
            }

    }


    class MonthYearPickerDialog() : DialogFragment() {
        private var listener: DatePickerDialog.OnDateSetListener? = null
        fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
            this.listener = listener
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
            // Get the layout inflater
            val inflater: LayoutInflater = requireActivity().getLayoutInflater()
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

    fun logOut(view: View){
        val sp = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sp.edit()
        val i = -1
        editor.putInt(DbHelper.USER_ID, i)
        editor.apply()
        startActivity(Intent(this, Login::class.java))
    }
}