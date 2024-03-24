package com.example.todolistmultiplatform.android.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolistmultiplatform.android.enums.SortOption
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.utils.getFreeId

@Composable
fun NavGraph(
    navController: NavHostController,
    todoList: List<Todo>,
    onAddTodo: (List<Todo>) -> Unit,
    onDeleteTask: (Todo) -> Unit,
    onCheckboxClicked: (Todo, Boolean) -> Unit,
    sortOption: SortOption,
    onSortOptionSelected :  (SortOption) -> Unit
) {
    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            AppMainScreen(
                todoList = todoList,
                onNavigateToTaskCreation = {
                    navController.navigate("task_creation")
                },
                onDeleteTask = onDeleteTask,
                onCheckboxClicked = onCheckboxClicked,
                sortOption = sortOption,
                onSortOptionSelected = onSortOptionSelected
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
