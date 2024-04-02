package com.example.todolistmultiplatform.android.item

import com.google.gson.annotations.SerializedName
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Todo(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String?,
        @SerializedName("date") val date: String?,
        @SerializedName("file") val file: String?,
        @SerializedName("isDone") val isDone: Boolean
) {
        fun isOverdue(): Boolean {
                if (date == null) return false

                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                try {
                        val todoDate = formatter.parse(date)

                        // Add one day to the current date
                        val calendar = Calendar.getInstance()
                        calendar.time = Date()
                        calendar.add(Calendar.DAY_OF_YEAR, -1)
                        val currentDatePlusOneDay = calendar.time
                        return todoDate.before(currentDatePlusOneDay)
                } catch (e: ParseException) {
                        e.printStackTrace()
                        return false
                }
        }
}
