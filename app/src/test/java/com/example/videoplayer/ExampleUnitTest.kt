package com.example.videoplayer

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        setDate("2025-01-02T23:45:01Z")
    }


    fun convert2Cal(sdf: SimpleDateFormat, strDate: String): Calendar {
        return try {
            val parse = sdf.parse(strDate)
            if (parse != null) {
                val tempCal = Calendar.getInstance()
                tempCal.time = parse
                tempCal
            } else
                Calendar.getInstance()
        } catch (e: Exception) {
            Calendar.getInstance()
        }
    }

    private fun setDate(publishedDate: String) {

        val sdfDt25 =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH) //2025-01-03T10:57:01Z

        val inputFormatter = sdfDt25

        val dateTime = convert2Cal(inputFormatter, publishedDate).time


        val timeZone = TimeZone.getDefault()
        val offsetInMillis = timeZone.rawOffset + timeZone.dstSavings
        val offsetInMinutes = offsetInMillis / (60 * 1000)


        val calendar = Calendar.getInstance()
        calendar.time = dateTime
        calendar.add(Calendar.MINUTE, offsetInMinutes)
        val updatedDateTime = calendar.time

//            val now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
        val now = Calendar.getInstance(TimeZone.getTimeZone("UTC+05:30")).time

        println(dateTime)
        println(updatedDateTime)
    }
}