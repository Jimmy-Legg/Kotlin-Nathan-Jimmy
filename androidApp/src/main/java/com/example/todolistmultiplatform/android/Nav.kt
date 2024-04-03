package com.example.todolistmultiplatform.android

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolistmultiplatform.android.enums.SortOption
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.utils.getFreeId
import com.example.todolistmultiplatform.android.views.AddItemScreen
import com.example.todolistmultiplatform.android.views.AppMainScreen
import com.example.todolistmultiplatform.android.views.CongratsScreen
import com.example.todolistmultiplatform.android.views.ModifyItemScreen



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

    val isTransitioning = remember { mutableStateOf(true) }
    val currentBackStackEntry = remember { mutableStateOf(navController.currentBackStackEntry) }

    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            AppMainScreen(
                todoList = todoList,
                filteredTodoList = filteredTodoList,
                onNavigateToTaskCreation = {
                    if (!isTransitioning.value)
                    {
                        isTransitioning.value = true
                        navController.navigate("task_creation")
                        currentBackStackEntry.value = navController.currentBackStackEntry
                    }
                },
                onDeleteTask = onDeleteTask,
                onCheckboxClicked = { todo, isChecked ->
                    if (isChecked)
                    {
                        if(!isTransitioning.value)
                        {
                            isTransitioning.value = true
                            navController.navigate("congrats/${todo.id}")
                            currentBackStackEntry.value = navController.currentBackStackEntry
                        }
                    }
                    onCheckboxClicked(todo, isChecked)
                },
                sortOption = sortOption,
                onSortOptionSelected = onSortOptionSelected,
                onModifyTodo = { todo ->
                    if(!isTransitioning.value)
                    {
                        isTransitioning.value = true
                        navController.navigate("modify_todo/${todo.id}")
                        currentBackStackEntry.value = navController.currentBackStackEntry
                    }
                }
            )
        }

        composable("modify_todo/{todoId}", arguments = listOf(navArgument("todoId") { type = NavType.IntType }))
        { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId")
            val selectedTodo = remember { todoList.find { it.id == todoId } }
            if (selectedTodo != null)
            {
                ModifyItemScreen(
                    navController = navController,
                    todo = selectedTodo,
                    onModify = { modifiedTodo ->
                        val modifiedList = todoList.map {
                            if (it.id == modifiedTodo.id) modifiedTodo else it
                        }
                        onAddTodo(modifiedList)
                    },
                    onBack = {
                        if(!isTransitioning.value)
                        {
                            isTransitioning.value = true
                            navController.navigate("task_list")
                            currentBackStackEntry.value = navController.currentBackStackEntry
                        }
                    }
                )
            }
        }

        composable("task_creation") {
            AddItemScreen(
                navController = navController,
                onTaskCreated = { name: String, description: String?, date: String?, file: String? ->
                    if(!isTransitioning.value)
                    {
                        val newTodoList = todoList.toMutableList()
                        val newId = getFreeId(newTodoList)
                        newTodoList.add(Todo(newId, name, description, date, file, false))
                        onAddTodo(newTodoList)

                        isTransitioning.value = true
                        navController.navigate("task_list")
                        currentBackStackEntry.value = navController.currentBackStackEntry
                    }
                }
            )
        }

        composable("congrats/{todoId}", arguments = listOf(navArgument("todoId") { type = NavType.IntType }))
        {
            backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId")
            val selectedTodo = remember { todoList.find { it.id == todoId } }
            if (selectedTodo != null) {
                CongratsScreen(
                    todo = selectedTodo,
                    onBack = {
                        if(!isTransitioning.value)
                        {
                            isTransitioning.value = true
                            navController.navigate("task_list")
                            currentBackStackEntry.value = navController.currentBackStackEntry
                        }
                    }
                )
            }
        }
    }
    LaunchedEffect(key1 = currentBackStackEntry.value) {
        isTransitioning.value = false
        Log.d("log", isTransitioning.value.toString())
    }
}
