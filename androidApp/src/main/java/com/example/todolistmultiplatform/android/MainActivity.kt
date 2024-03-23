package com.example.todolistmultiplatform.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.views.AppMainScreen
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.IOException
import java.io.OutputStream

class MainActivity : ComponentActivity() {

    private var todoList by mutableStateOf(emptyList<Todo>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todoListJson = loadJsonFile("todo_list.json")
        todoList = parseTodoList(todoListJson ?: "[{\"name\": \"Task 1\", \"date\": \"2023-03-25\", \"isDone\": false},{\"name\": \"Task 2\", \"date\": \"2023-03-26\", \"isDone\": true}]")

        if (todoListJson == null) {
            createDefaultJsonFile("todo_list.json")
        }

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppMainScreen(todoList, onAddTodo = ::updateTodoList)
                }
            }
        }
    }

    private fun updateTodoList(newTodoList: List<Todo>) {
        todoList = newTodoList
    }
    private fun loadJsonFile(fileName: String): String? {
        return try {
            val inputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun createDefaultJsonFile(fileName: String) {
        try {
            val defaultJsonData = "[{\"name\": \"Task 1\", \"date\": \"2023-03-25\", \"isDone\": false},{\"name\": \"Task 2\", \"date\": \"2023-03-26\", \"isDone\": true}]"
            val outputStream: OutputStream = openFileOutput(fileName, MODE_PRIVATE)
            outputStream.write(defaultJsonData.toByteArray())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parseTodoList(json: String): List<Todo> {
        val gson = Gson()
        val listType = object : TypeToken<List<Todo>>() {}.type
        return gson.fromJson<List<Todo>>(json, listType)
    }
}