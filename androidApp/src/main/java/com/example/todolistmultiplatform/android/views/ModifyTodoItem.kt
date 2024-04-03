package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistmultiplatform.android.composable.TaskFormItem
import com.example.todolistmultiplatform.android.composable.TopBarApp
import com.example.todolistmultiplatform.android.item.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTodoItemScreen(
    navController: NavHostController,
    todo: Todo,
    onModify: (Todo) -> Unit,
    onBack: () -> Unit
) {
    val name = remember { mutableStateOf(todo.name) }
    val description = remember { mutableStateOf(todo.description ?: "") }
    val date = remember { mutableStateOf(todo.date ?: "") }
    val file = remember { mutableStateOf(todo.file ?: "") }

    val isNameValid = remember { mutableStateOf(true) }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .verticalScroll(rememberScrollState())
    ) {
        TopBarApp(navController = navController, pageName = "Modify")

        TaskFormItem(name, description, date, file, isNameValid)

        Spacer(modifier = Modifier.height(8.dp))

        FloatingActionButton(
            onClick = {
                val modifiedTodo = todo.copy(name = name.value, description = description.value, date = date.value, file = file.value)
                onModify(modifiedTodo)
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.inversePrimary,
            content = {
                Text("Modifier la tâche", color = MaterialTheme.colorScheme.primary)
            }
        )
    }
}
