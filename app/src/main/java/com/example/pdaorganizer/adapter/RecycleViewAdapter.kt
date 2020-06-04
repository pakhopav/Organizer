package com.example.pdaorganizer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.db.DbHelper
import com.example.pdaorganizer.model.Issue

class RecycleViewAdapter(private val context: Context , private val issueList: ArrayList<Issue>) : RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {



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
            "shop" -> holder.categoryImage.setImageResource(R.drawable.ic_shop)
            else -> { // Note the block
                holder.categoryImage.setVisibility(View.GONE)
            }
        }

        holder.issueName.setText(issueList[position].name)
        holder.issueDeadline.setText(issueList[position].deadline)

        holder.deleteButton.setOnClickListener {
            val dbHelper = DbHelper(context)
            dbHelper.deleteIssue(issueList[position])
            removeItem(position)
        }
    }

    override fun getItemCount() = issueList.size

    fun removeItem(position: Int){
        issueList.removeAt(position)
        notifyDataSetChanged()
    }
}