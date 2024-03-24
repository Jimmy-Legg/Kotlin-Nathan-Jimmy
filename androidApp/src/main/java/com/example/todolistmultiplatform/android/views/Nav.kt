package com.example.todolistmultiplatform.android.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.utils.getFreeId

@Composable
fun NavGraph(
    navController: NavHostController,
    todoList: List<Todo>,
    onAddTodo: (List<Todo>) -> Unit,
    onDeleteTask: (Todo) -> Unit,
    onCheckboxClicked: (Todo, Boolean) -> Unit // Add this parameter
) {
    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            AppMainScreen(
                todoList = todoList,
                onNavigateToTaskCreation = {
                    navController.navigate("task_creation")
                },
                onDeleteTask = onDeleteTask,
                onCheckboxClicked = onCheckboxClicked // Pass down the callback
            )
        }
        composable("task_creation") {
            TaskCreationScreen(
                navController = navController,
                onTaskCreated = { name : String, date : String ->
                    val newTodoList = todoList.toMutableList()
                    val newId = getFreeId(newTodoList)
                    newTodoList.add(Todo(newId, name, date, false))
                    onAddTodo(newTodoList)
                    navController.popBackStack()
                }
            )
        }
    }
}
