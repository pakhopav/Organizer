package com.example.pdaorganizer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdaorganizer.R
import com.example.pdaorganizer.activities.Register
import com.example.pdaorganizer.activities.StatisticsOfDay
import com.example.pdaorganizer.model.Day
import com.example.pdaorganizer.model.Issue

class DaysRecycleAdapter(private val context: Context,private val MY :String, private val dayList: ArrayList<Day>) : RecyclerView.Adapter<DaysRecycleAdapter.MyViewHolder>() {
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val dateText : TextView
        val closed : TextView
        val more : Button

        init {
            dateText = view.findViewById(R.id.dayRowDate)
            closed = view.findViewById(R.id.dayRowClosed)
            more  = view.findViewById(R.id.dayRowBtn)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.day_row, parent,false)
        return DaysRecycleAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int = dayList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dateText.setText("${dayList[position].d+1} ${MY.split(" ")[0]}")
        holder.closed.setText(dayList[position].i.toString())
        holder.more.setOnClickListener {
            val intent = Intent(context, StatisticsOfDay::class.java).putExtra("date", "${dayList[position].d+1} $MY")
            context.startActivity(intent)
        }

    }
}