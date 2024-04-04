package com.example.todolistmultiplatform.android

import JsonUtils.loadFile
import JsonUtils.saveTodoList
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.todolistmultiplatform.android.enums.SortOption
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.theme.MyApplicationTheme
import com.example.todolistmultiplatform.android.widget.TodoWidgetProvider

class MainActivity : ComponentActivity() {

    private var todoList
    by mutableStateOf(emptyList<Todo>())

    private var sortOption by mutableStateOf(SortOption.Tous)
    var filteredTodoList by mutableStateOf(emptyList<Todo>())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val componentName = ComponentName(this, TodoWidgetProvider::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        if (appWidgetIds.isNotEmpty()) {
            TodoWidgetProvider().onUpdate(this, appWidgetManager, appWidgetIds)
        }
        todoList = loadFile(this)
        filteredTodoList = todoList

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
                        filteredTodoList = filteredTodoList,
                        onAddTodo = { newTodoList -> updateTodoList(newTodoList) },
                        onDeleteTask = { deletedTodo -> deleteTask(deletedTodo) },
                        onCheckboxClicked = { todo, isChecked -> updateTodoStatus(todo, isChecked) },
                        sortOption,
                        onSortOptionSelected = { sortOption -> changeSortOption(sortOption) }
                    )
                }
            }
        }
    }

    private fun deleteTask(todo: Todo) {
        val updatedTodoList = todoList.filterNot { it == todo }
        updateTodoList(updatedTodoList)
    }

    private fun changeSortOption(newSortOption: SortOption) {
        sortOption = newSortOption
        updateTodoListUI(todoList)
    }

    private fun updateTodoList(newTodoList: List<Todo>) {
        todoList = newTodoList
        updateTodoListUI(todoList)
        saveTodoList(this, newTodoList)
    }

    private fun updateTodoListUI(newTodoList: List<Todo>) {
        filteredTodoList = when (sortOption) {
            SortOption.Tous -> todoList.sortedBy { it.date ?: "" }
            SortOption.Fait -> todoList.filter { it.isDone }.sortedBy { it.date ?: "" }
            SortOption.PasFait -> todoList.filterNot { it.isDone }.sortedBy { it.date ?: "" }
            SortOption.Dépassé -> todoList.filter { it.isOverdue() }.sortedBy { it.date ?: "" }
        }
    }

    private fun updateTodoStatus(todo: Todo, isChecked: Boolean) {
        val updatedTodoList = todoList.map { if (it == todo) it.copy(isDone = isChecked) else it }
        updateTodoList(updatedTodoList)
    }
}