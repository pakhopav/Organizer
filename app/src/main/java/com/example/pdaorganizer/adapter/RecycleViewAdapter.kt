package com.example.pdaorganizer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.model.Issue

class RecycleViewAdapter(private val context: Context , private val issueList: Array<Issue>) : RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {



    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val issueName : TextView
        val deleteButton : ImageButton
        val infoButton : ImageButton
        val importanceView : View
        val categoryImage : ImageView
        val issueDeadline : TextView
        init {
            issueName = view.findViewById(R.id.issueNameText)
            deleteButton = view.findViewById(R.id.issueDeleteButton)
            infoButton  = view.findViewById(R.id.issueInfoButton)
            importanceView = view.findViewById(R.id.issueImportanceSquare)
            categoryImage = view.findViewById(R.id.issueCategoryImage)
            issueDeadline = view.findViewById(R.id.issueDeadlineText)

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.issue_row, parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.issueName.setText(issueList[position].name)
        holder.issueDeadline.setText(issueList[position].deadline)
    }

    override fun getItemCount() = issueList.size
}