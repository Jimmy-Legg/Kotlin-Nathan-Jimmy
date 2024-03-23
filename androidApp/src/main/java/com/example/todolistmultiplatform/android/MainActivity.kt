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
import androidx.navigation.compose.rememberNavController
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.views.NavGraph
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.IOException
import java.io.OutputStream

class MainActivity : ComponentActivity() {

    private var todoList by mutableStateOf(emptyList<Todo>())

    private var defaultJson = "[]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todoListJson = loadJsonFile()
        todoList = parseTodoList(todoListJson ?: defaultJson)

        if (todoListJson == null) {
            createDefaultJsonFile()
        }

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        todoList = todoList,
                        onAddTodo = { newTodoList -> updateTodoList(newTodoList) },
                        onDeleteTask = { deletedTodo -> deleteTask(deletedTodo) },
                        onCheckboxClicked = { todo, isChecked -> updateTodoStatus(todo, isChecked) }
                    )
                }
            }
        }
    }

    private fun deleteTask(todo: Todo) {
        val updatedTodoList = todoList.filterNot { it == todo }
        updateTodoList(updatedTodoList)
    }

    private fun updateTodoList(newTodoList: List<Todo>) {
        todoList = newTodoList
        saveTodoList(newTodoList)
    }

    private fun updateTodoStatus(todo: Todo, isChecked: Boolean) {
        val updatedTodoList = todoList.map { if (it == todo) it.copy(isDone = isChecked) else it }
        updateTodoList(updatedTodoList)
    }

    private fun saveTodoList(todoList: List<Todo>) {
        try {
            val gson = Gson()
            val jsonData = gson.toJson(todoList)
            val outputStream: OutputStream = openFileOutput("todo_list.json", MODE_PRIVATE)
            outputStream.write(jsonData.toByteArray())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadJsonFile(): String? {
        return try {
            val inputStream = openFileInput("todo_list.json")
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

    private fun createDefaultJsonFile() {
        try {
            val outputStream: OutputStream = openFileOutput("todo_list.json", MODE_PRIVATE)
            outputStream.write(defaultJson.toByteArray())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parseTodoList(json: String): List<Todo> {
        val gson = Gson()
        val listType = object : TypeToken<List<Todo>>() {}.type
        return gson.fromJson(json, listType)
    }
}