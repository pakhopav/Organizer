package com.example.pdaorganizer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.helpers.DateHelper
import com.example.pdaorganizer.activities.IssueInfo
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.dialogs.CloseIssueDialog
import com.example.pdaorganizer.dialogs.OrganizerDialogListener
import com.example.pdaorganizer.model.Issue
import java.text.SimpleDateFormat

class RecycleViewAdapter(private val context: Context , private val issueList: ArrayList<Issue>) : RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {
    val helper = DateHelper()


    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val issueName : TextView
        val deleteButton : ImageButton
        val infoButton : ImageButton
        val importanceView : View

        var categoryImage : ImageView
        val issueDeadline : TextView
        init {
            issueName = view.findViewById(R.id.issueNameText)
            deleteButton = view.findViewById(R.id.issueDeleteButton)
            infoButton  = view.findViewById(R.id.issueInfoButton)
            importanceView = view.findViewById(R.id.issueImportanceSquare)
            categoryImage = view.findViewById(R.id.issueCategoryRowImage)
            issueDeadline = view.findViewById(R.id.issueDeadlineText)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.issue_row, parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var color =-1;
        when(issueList[position].importance){
            "Low" -> color = ContextCompat.getColor(context, R.color.colorImportanceL)
            "Mid" -> color = ContextCompat.getColor(context, R.color.colorImportanceM)
            "High" -> color = ContextCompat.getColor(context, R.color.colorImportanceH)
        }
        holder.importanceView.setBackgroundColor(color)

        when(issueList[position].category){
            "Debt" -> holder.categoryImage.setImageResource(R.drawable.ic_money)
            "School" -> holder.categoryImage.setImageResource(R.drawable.ic_school)
            "Work" -> holder.categoryImage.setImageResource(R.drawable.ic_work)
            "Shop" -> holder.categoryImage.setImageResource(R.drawable.ic_shop)
            else -> { // Note the block
            }
        }

        holder.issueName.setText(issueList[position].name)
        holder.issueDeadline.setText(issueList[position].deadline)

        if(!issueList[position].deadline.equals("")){
            val daysToExpire = helper.getDifferenceFromDateToToday(SimpleDateFormat.getDateInstance().parse(issueList[position].deadline))
            holder.issueDeadline.setText("${daysToExpire}d")
            if(daysToExpire <0){
                holder.issueDeadline.setTextColor(ContextCompat.getColor(context, R.color.colorImportanceH))
                val newIssue = Issue(id = issueList[position].id,
                    name = issueList[position].name,
                    user = issueList[position].user,
                    category = issueList[position].category,
                    description =  issueList[position].description,
                    importance = issueList[position].importance,
                    photoPath = issueList[position].photoPath,
                    active = "e",
                    deadline = issueList[position].deadline,
                    closeDate = issueList[position].closeDate)
                updateIssue(newIssue)
            }
        }else{
            holder.issueDeadline.setText("-")
        }



        holder.deleteButton.setOnClickListener {
            val dialog = CloseIssueDialog()
            dialog.setMyListener(object : OrganizerDialogListener {
                override fun onOk() {
                    var active = "f"
                    if(issueList[position].active == "e") active = "ex"
                    val today = helper.getDaysToString()
                    val newIssue = Issue(id = issueList[position].id,
                        name = issueList[position].name,
                        user = issueList[position].user,
                        category = issueList[position].category,
                        description =  issueList[position].description,
                        importance = issueList[position].importance,
                        photoPath = issueList[position].photoPath,
                        active = active,
                        deadline = issueList[position].deadline,
                        closeDate = today)

                    updateIssue(newIssue)
                    removeItem(position)
                }
            })

            dialog.show((context as AppCompatActivity).supportFragmentManager, "close dialog")

        }

        holder.infoButton.setOnClickListener {
            val sp = context.getSharedPreferences(DbHelper.SHARED_PREFS, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putInt(DbHelper.ISSUE_ID, issueList[position].id)
            editor.apply()
            context.startActivity(Intent(context, IssueInfo::class.java))
        }
//        if(issueList[position].active.equals("f")) holder.deleteButton.visibility = View.GONE



    }

    override fun getItemCount() = issueList.size

    fun removeItem(position: Int){
        issueList.removeAt(position)
        notifyDataSetChanged()
    }

    fun updateIssue(issue: Issue){
        val dbHelper = DbHelper(context)
        dbHelper.updateIssue(issue)
    }
}