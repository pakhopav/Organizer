package com.example.pdaorganizer.adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.pdaorganizer.R
import com.example.pdaorganizer.model.IssueImportance

class IssueImportanceSpinnerAdapter(private val myContext: Context,private val array: ArrayList<IssueImportance> ) : ArrayAdapter<IssueImportance>(myContext,0, array){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position,convertView,parent)
    }

    fun initView(position: Int, convertView: View?, parent: ViewGroup): View{
        val importance = getItem(position)
        if(importance != null){
            if(convertView == null){
                val convertView2 = LayoutInflater.from(context).inflate(R.layout.issue_importance_spinner_row,parent, false)
                convertView2.findViewById<View>(R.id.issueImportanceSpinnerRowImage).setBackgroundColor(importance.color)
                convertView2.findViewById<TextView>(R.id.issueImportanceSpinnerRowText).setText(importance.name)
                return convertView2
            }else{
                convertView.findViewById<View>(R.id.issueImportanceSpinnerRowImage).setBackgroundColor(importance.color)
                convertView.findViewById<TextView>(R.id.issueImportanceSpinnerRowText).setText(importance.name)
                return convertView
            }
        }
       return convertView!!


    }
}