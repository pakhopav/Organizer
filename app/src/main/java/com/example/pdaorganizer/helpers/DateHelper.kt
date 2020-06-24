package com.example.pdaorganizer.helpers

import java.util.*

class DateHelper {
    val monthArray = arrayListOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
    fun getDifferenceFromDateToToday(date1: Date): Int{
        val date2 = Calendar.getInstance().getTime()
        val difference = (date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        return (differenceDates+1).toInt()
    }

    fun getdays(): Long{
        val date = Calendar.getInstance().getTime()
        val r =date.time / (24 * 60 * 60 * 1000)
        return  r
    }

    fun getDaysToString(): String{
        return Calendar.getInstance().getTime().toString()
    }
    fun getDaysToString(d :Date): String{
        return Calendar.getInstance().getTime().toString()
    }

    fun getdays(str: String): Long{
        val strArr = str.split(" ")
        val y = strArr.last().toInt() -1900
        val m = monthArray.indexOf(strArr[1])
        val d = strArr[2].toInt()
        return  getdays(y, m,d)
    }

    fun getdays(y: Int, m : Int, d : Int): Long{
        val date = Date(y, m ,d)
        val r =date.time / (24 * 60 * 60 * 1000)
        return  r
    }

    fun parse(n : Int):Date{
        val y : Int = n/365
        val m : Int = (n % 365) / 30
        val d : Int = (n % 365) % 30
        val date = Date(y,m,d)
        return date
    }
}