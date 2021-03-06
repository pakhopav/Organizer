package com.example.pdaorganizer.activities

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pdaorganizer.R
import com.example.pdaorganizer.adapter.DatePickFragment
import com.example.pdaorganizer.adapter.IssueCategorySpinnerAdapter
import com.example.pdaorganizer.adapter.IssueImportanceSpinnerAdapter
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.helpers.DateHelper
import com.example.pdaorganizer.helpers.StorageHelper
import com.example.pdaorganizer.model.Issue
import com.example.pdaorganizer.model.IssueCategory
import com.example.pdaorganizer.model.IssueImportance
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewIssue : AppCompatActivity() , DatePickerDialog.OnDateSetListener {
    private lateinit var bottomNavigationView :BottomNavigationView

    private lateinit var nameInput : EditText
    private lateinit var descriptionInput : EditText
    private lateinit var button: Button
    private lateinit var errorMessage : TextView
    private lateinit var deadlineText : TextView
    private lateinit var image :ImageView
    private lateinit var delImgBtn :ImageButton
    private lateinit var bigImage: ImageView



    private lateinit var dbHelper: DbHelper
    private lateinit var categorySpinner: Spinner
    private lateinit var categoryArrayList: ArrayList<IssueCategory>
    private lateinit var categorySpinnerAdapter : IssueCategorySpinnerAdapter
    private lateinit var importanceSpinner: Spinner
    private lateinit var importanceSpinnerAdapter : IssueImportanceSpinnerAdapter
    private  lateinit var storageHelper: StorageHelper

    private val REQUEST_IMAGE_CAPTURE = 1
    private var deadlineDate = ""
    private var photoPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_issue)

        initNavBar()

        initViews()

        initObjects()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            findViewById<ImageButton>(R.id.imageButton3).setEnabled(false)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )
        }
    }


    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(1).setChecked(true)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

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
        nameInput = findViewById(R.id.issueName)
        descriptionInput = findViewById(R.id.issueDescription)
        button = findViewById(R.id.issueCreate)
        categorySpinner = findViewById(R.id.issueCategorySpinner)
        importanceSpinner = findViewById(R.id.issueImportanceSpinner)
        errorMessage =findViewById(R.id.newIssueError)
        errorMessage.visibility = View.GONE
        image = findViewById(R.id.newissueImage)
        delImgBtn = findViewById(R.id.newissueDeleteImage)
        deadlineText = findViewById(R.id.newIssueDeadlineText)
    }

    fun initObjects(){
        dbHelper = DbHelper(this)
        storageHelper = StorageHelper(this)


        categoryArrayList = ArrayList()
        categoryArrayList.add(IssueCategory(R.drawable.ic_no_category, "No category"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_money, "Debt"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_school, "School"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_work, "Work"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_shop, "Shop"))

        categorySpinnerAdapter = IssueCategorySpinnerAdapter(this, categoryArrayList)
        categorySpinner.adapter = categorySpinnerAdapter
        categorySpinner.setVisibility(View.VISIBLE)


        val green = ContextCompat.getColor(this,
            R.color.colorImportanceL
        )
        val yellow = ContextCompat.getColor(this,
            R.color.colorImportanceM
        )
        val red = ContextCompat.getColor(this,
            R.color.colorImportanceH
        )
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
                            photoPath = photoPath,
                            active = "t",
                            deadline = deadlineDate,
                            closeDate = "")
        dbHelper.addIssue(newIssue)


    }


    fun openDialog(view: View){
        val dialog = DatePickFragment()
        dialog.show(supportFragmentManager, "date picker")
    }
    fun issueCreationButtonOnClick(view: View){
        if(nameInput.text.toString().trim().equals("")){
            errorMessage.setText("Enter issue name")
            errorMessage.visibility = View.VISIBLE
        }else{
            errorMessage.visibility = View.GONE
            createIssue()
            clearInputs()
            val toast = Toast.makeText(this,"Issue created",Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0,520 )
            toast.show()
        }
    }

    fun clearInputs(){
        nameInput.setText("")
        descriptionInput.setText("")
        categorySpinner.setSelection(0)
        importanceSpinner.setSelection(0)
        delImage(delImgBtn)
        deadlineDate = ""
        deadlineText.setText("")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        if(DateHelper().getDifferenceFromDateToToday(c.time)>=0){
            deadlineDate = SimpleDateFormat.getDateInstance().format(c.time)
            deadlineText.setText(deadlineDate)
            errorMessage.visibility = View.GONE

        }else{
            errorMessage.setText("Invalid expiration date")
            errorMessage.visibility = View.VISIBLE
        }


    }


    fun takePicture(view: View){
       dispatchTakePictureIntent()
    }







    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data?.extras
            val imageBitmap =extras!!["data"]  as Bitmap
            image.setImageBitmap(imageBitmap)
            image.visibility = View.VISIBLE
            delImgBtn.visibility = View.VISIBLE
            val s = storageHelper.saveToInternalStorage(imageBitmap)
            if(s != null) photoPath = s
        }
    }


    fun delImage(view: View){
        photoPath = ""
        image.visibility = View.GONE
        delImgBtn.visibility = View.GONE
    }

    fun openBigImage(view: View){
        bigImage.visibility = View.VISIBLE
    }
    fun hideBigImage(view: View){
        bigImage.visibility = View.GONE
    }



}