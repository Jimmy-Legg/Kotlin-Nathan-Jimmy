package com.example.todolistmultiplatform.android

import JsonUtils.loadFile
import JsonUtils.saveTodoList
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
import com.example.todolistmultiplatform.android.enums.SortOption
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.views.NavGraph

class MainActivity : ComponentActivity() {

    private var todoList by mutableStateOf(emptyList<Todo>())

    private var sortOption by mutableStateOf(SortOption.All)
    private var filteredTodoList by mutableStateOf(emptyList<Todo>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        todoList = filteredTodoList,
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
            SortOption.All -> todoList.sortedBy { it.date }
            SortOption.Done -> todoList.filter { it.isDone }.sortedBy { it.date }
            SortOption.NotDone -> todoList.filterNot { it.isDone }.sortedBy { it.date }
        }
    }

    private fun updateTodoStatus(todo: Todo, isChecked: Boolean) {
        val updatedTodoList = todoList.map { if (it == todo) it.copy(isDone = isChecked) else it }
        updateTodoList(updatedTodoList)
    }
}