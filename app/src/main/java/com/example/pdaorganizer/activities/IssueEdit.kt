package com.example.pdaorganizer.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.pdaorganizer.*
import com.example.pdaorganizer.adapter.DatePickFragment
import com.example.pdaorganizer.adapter.IssueCategorySpinnerAdapter
import com.example.pdaorganizer.adapter.IssueImportanceSpinnerAdapter
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.helpers.StorageHelper
import com.example.pdaorganizer.model.Issue
import com.example.pdaorganizer.model.IssueCategory
import com.example.pdaorganizer.model.IssueImportance
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IssueEdit : AppCompatActivity() , DatePickerDialog.OnDateSetListener {

    private lateinit var bottomNavigationView : BottomNavigationView

    private lateinit var nameInput : EditText
    private lateinit var descriptionInput : EditText
    private lateinit var deadlineText : TextView
    private lateinit var image : ImageView
    private lateinit var imageLabel: TextView
    private lateinit var delImgBtn :ImageButton


    private lateinit var dbHelper: DbHelper
    private lateinit var storageHelper: StorageHelper
    private lateinit var categorySpinner: Spinner
    private lateinit var categoryArrayList: ArrayList<IssueCategory>
    private lateinit var categorySpinnerAdapter : IssueCategorySpinnerAdapter
    private lateinit var importanceSpinner: Spinner
    private lateinit var importanceSpinnerAdapter : IssueImportanceSpinnerAdapter
    private lateinit var myIssue: Issue
    private val REQUEST_IMAGE_CAPTURE = 1

    private var deadlineDate =""
    private var photoPath =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue_edit)

        dbHelper = DbHelper(this)
        storageHelper = StorageHelper(this)
        val extras = intent.extras
        val issue = dbHelper.getIssueById(extras!!.getInt("id"))
        if(issue != null){
            myIssue = issue
        }

        initNavBar()

        initViews()

        initObjects()

    }

    fun initNavBar(){
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(1).setChecked(true)
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
        nameInput = findViewById(R.id.editIssueName)
        descriptionInput = findViewById(R.id.editIssueDescription)
        categorySpinner = findViewById(R.id.editIssueCategorySpinner)
        importanceSpinner = findViewById(R.id.editIssueImportanceSpinner)
        deadlineText = findViewById(R.id.editIssueDeadlineText)
        imageLabel = findViewById(R.id.editIssueImageLabel)
        image = findViewById(R.id.editIssueImage)
        delImgBtn = findViewById(R.id.editIssueDeleteImage)

        nameInput.setText(myIssue.name)
        descriptionInput.setText(myIssue.description)
        deadlineText.setText(myIssue.deadline)



    }

    fun initObjects(){


        categoryArrayList = ArrayList()
        categoryArrayList.add(IssueCategory(R.drawable.ic_no_category, "No category"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_money, "Debt"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_school, "School"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_work, "Work"))
        categoryArrayList.add(IssueCategory(R.drawable.ic_shop, "Shop"))

        categorySpinnerAdapter = IssueCategorySpinnerAdapter(this, categoryArrayList)
        categorySpinner.adapter = categorySpinnerAdapter
        categorySpinner.visibility = View.VISIBLE

        var categorySelection = 0
        when(myIssue.category){
            "No category" -> categorySelection= 0
            "Debt" -> categorySelection= 1
            "School" -> categorySelection= 2
            "Work" -> categorySelection= 3
            "Shop" -> categorySelection= 4

        }
        categorySpinner.setSelection(categorySelection)


        val green = ContextCompat.getColor(this,
            R.color.colorImportanceL
        )
        val yellow = ContextCompat.getColor(this,
            R.color.colorImportanceM
        )
        val red = ContextCompat.getColor(this,
            R.color.colorImportanceH
        )
        val array = arrayListOf(
            IssueImportance(green, "Low importance"),
            IssueImportance(yellow, "Mid importance"),
            IssueImportance(red, "High importance")
        )
        importanceSpinnerAdapter = IssueImportanceSpinnerAdapter(this, array)
        importanceSpinner.adapter = importanceSpinnerAdapter
        importanceSpinner.visibility = View.VISIBLE
        var impSelection = 0
        when(myIssue.importance){
            "Low" -> impSelection= 0
            "Mid" -> impSelection= 1
            "High" -> impSelection= 2
        }
        importanceSpinner.setSelection(impSelection)


        if(myIssue.photoPath.equals("")){
            image.visibility= View.GONE
            imageLabel.setText("No photo")
        }else{
            image.visibility= View.VISIBLE
            delImgBtn .visibility= View.VISIBLE
            image.setImageBitmap(storageHelper.loadImageFromStorage(myIssue.photoPath))
        }
    }

    fun openDialog(view: View){
        val dialog = DatePickFragment()
        dialog.show(supportFragmentManager, "date picker")
    }
    fun saveChanges(view: View){
        val selectedCategoryObj =  categorySpinner.selectedItem as IssueCategory
        val selectedCategory = selectedCategoryObj.name
        val selectedImportanceObj = importanceSpinner.selectedItem as IssueImportance
        val selectedImportance = selectedImportanceObj.name.split(" ")[0]

        val sh = getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE)
        val creator = sh.getInt(DbHelper.USER_ID, -1)
        val newIssue = Issue(id = myIssue.id,
            name = nameInput.text.toString().trim(),
            user = creator,
            category = selectedCategory,
            description =  descriptionInput.text.toString().trim(),
            importance = selectedImportance,
            photoPath = photoPath,
            active = myIssue.active,
            deadline = deadlineDate,
            closeDate = myIssue.closeDate)

        dbHelper.updateIssue(newIssue)
        startActivity(Intent(this, IssueInfo::class.java).putExtra("id", myIssue.id))
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        deadlineDate = SimpleDateFormat.getDateInstance().format(c.time)
        findViewById<TextView>(R.id.editIssueDeadlineText).setText(deadlineDate)

    }



    fun takePictureEdit(view: View){
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
            imageLabel.setText("Photo:")
            val s = storageHelper.saveToInternalStorage(imageBitmap)
            if(s != null) photoPath = s
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                findViewById<TextView>(R.id.newIssueDeadlineText).isEnabled = true;
            }
        }
    }


    fun delImage(view: View){
        photoPath = ""
        image.visibility = View.GONE
        delImgBtn.visibility = View.GONE
        imageLabel.setText("No photo")
    }
}