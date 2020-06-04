package com.example.pdaorganizer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.model.IssueCategory

class IssueCategorySpinnerAdapter(private val myContext: Context ,private val list: ArrayList<IssueCategory>) : ArrayAdapter<IssueCategory>(myContext,0,list){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position,convertView,parent)
    }

    fun initView(position: Int, convertView: View?, parent: ViewGroup): View{
        val category =getItem(position)
        if(category!= null){
            if(convertView == null){
                val convertView2 = LayoutInflater.from(context).inflate(R.layout.issue_category_spinner_row,parent, false)
                convertView2.findViewById<ImageView>(R.id.issueCategoryRowImage).setImageResource(category.imgId)
                convertView2.findViewById<TextView>(R.id.issueCategoryRowName).setText(category.name)
                return convertView2
            }else{
                convertView.findViewById<ImageView>(R.id.issueCategoryRowImage).setImageResource(category.imgId)
                convertView.findViewById<TextView>(R.id.issueCategoryRowName).setText(category.name)
                return convertView
            }
        }
        return convertView!!

    }
}