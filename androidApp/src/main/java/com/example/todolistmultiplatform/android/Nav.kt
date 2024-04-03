package com.example.todolistmultiplatform.android

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
import com.example.todolistmultiplatform.android.views.AppMainScreen
import com.example.todolistmultiplatform.android.views.CongratsScreen
import com.example.todolistmultiplatform.android.views.ModifyTodoItemScreen
import com.example.todolistmultiplatform.android.views.TaskCreationScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    todoList: List<Todo>,
    filteredTodoList: List<Todo>,
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
                filteredTodoList = filteredTodoList,
                onNavigateToTaskCreation = {
                    navController.navigate("task_creation")
                },
                onDeleteTask = onDeleteTask,
                onCheckboxClicked = onCheckboxClicked,
                sortOption = sortOption,
                onSortOptionSelected = onSortOptionSelected,
                onModifyTodo = { todo ->
                    navController.navigate("modify_todo/${todo.id}")
                }
            )
        }
        composable(
            "modify_todo/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId")
            val selectedTodo = remember { todoList.find { it.id == todoId } }
            if (selectedTodo != null) {
                ModifyTodoItemScreen(
                    navController = navController,
                    todo = selectedTodo,
                    onModify = { modifiedTodo ->
                        val modifiedList = todoList.map {
                            if (it.id == modifiedTodo.id) modifiedTodo else it
                        }
                        onAddTodo(modifiedList)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable("task_creation") {
            TaskCreationScreen(
                navController = navController,
                onTaskCreated = { name: String, description: String?, date: String?, file: String? ->
                    val newTodoList = todoList.toMutableList()
                    val newId = getFreeId(newTodoList)
                    newTodoList.add(Todo(newId, name, description, date, file, false))
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
                CongratsScreen(
                    todo = selectedTodo
                ) { navController.popBackStack() }
            }
        }
    }
}
