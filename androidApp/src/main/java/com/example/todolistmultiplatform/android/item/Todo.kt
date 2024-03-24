package com.example.todolistmultiplatform.android.item

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class Todo(
        @SerializedName("id") val id : Int,
        @SerializedName("name") val name: String,
        @SerializedName("date") val date: String?,
        @SerializedName("isDone") val isDone: Boolean,
) {
        fun isOverdue(): Boolean {
                if (date == null) return false

                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                try {
                        val todoDate = formatter.parse(date)
                        val currentDate = Date()
                        return todoDate.before(currentDate)
                } catch (e: ParseException) {
                        e.printStackTrace()
                        return false
                }
        }
}