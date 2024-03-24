package com.example.todolistmultiplatform.android.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolistmultiplatform.android.enums.SortOption
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.utils.getFreeId

@RequiresApi(Build.VERSION_CODES.O)
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
                onTaskCreated = { name: String, date: String? -> // Accept nullable String for date
                    val newTodoList = todoList.toMutableList()
                    val newId = getFreeId(newTodoList)
                    newTodoList.add(Todo(newId, name, date, false))
                    onAddTodo(newTodoList)
                    navController.popBackStack()
                }
            )
        }
        composable(
            "congrats/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId")
            val selectedTodo = remember { todoList.find { it.id == todoId } }
            if (selectedTodo != null) {
                CongratsScreen(navController = navController, backStackEntry = backStackEntry, todo = selectedTodo, onBackPressed = { navController.popBackStack() })
            }
        }
    }
}
