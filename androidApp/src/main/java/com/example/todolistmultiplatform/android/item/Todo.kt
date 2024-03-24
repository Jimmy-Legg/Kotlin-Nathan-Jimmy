package com.example.todolistmultiplatform.android.item

import com.google.gson.annotations.SerializedName

data class Todo(
        @SerializedName("id") val id : Int,
        @SerializedName("name") val name: String,
        @SerializedName("date") val date: String?,
        @SerializedName("isDone") val isDone: Boolean
)