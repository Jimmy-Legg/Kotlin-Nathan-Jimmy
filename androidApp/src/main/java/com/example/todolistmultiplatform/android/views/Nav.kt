package com.example.todolistmultiplatform.android.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolistmultiplatform.android.item.Todo

@Composable
fun NavGraph(navController: NavHostController, todoList: List<Todo>, onAddTodo: (List<Todo>) -> Unit) {
    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            AppMainScreen(todoList, onNavigateToTaskCreation = {
                navController.navigate("task_creation")
            })
        }
        composable("task_creation") {
            TaskCreationScreen(
                navController,
                onTaskCreated = { todo ->
                    val newTodoList = todoList.toMutableList()
                    newTodoList.add(todo)
                    onAddTodo(newTodoList)
                    navController.popBackStack()
                }
            )
        }
    }
}