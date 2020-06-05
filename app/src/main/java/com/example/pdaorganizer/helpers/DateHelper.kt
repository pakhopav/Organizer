package com.example.pdaorganizer.helpers

import java.util.*

class DateHelper {
    fun getDifferenceFromDateToToday(date1: Date): Int{
        val date2 = Calendar.getInstance().getTime()
        val difference = Math.abs(date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        return (differenceDates+1).toInt()
    }

    fun getdays(): Long{
        val date = Calendar.getInstance().getTime()
        val r =date.time / (24 * 60 * 60 * 1000)
        return  r
    }
    fun getdays(y: Int, m : Int, d : Int): Long{
        val date = Date(y, m ,d)
        val r =date.time / (24 * 60 * 60 * 1000)
        return  r
    }
}