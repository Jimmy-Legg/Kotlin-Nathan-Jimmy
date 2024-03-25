package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistmultiplatform.android.item.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTodoItemScreen(
    navController: NavHostController,
    todo: Todo,
    onModify: (Todo) -> Unit,
    onBack: () -> Unit
) {
    var newName by remember { mutableStateOf(todo.name) }
    var newDate by remember { mutableStateOf(todo.date ?: "") }

    Column {
        // Add a TopAppBar with a navigation icon (back button)
        TopBarApp(navController = navController, pageName = "Modify")
        TextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Todo Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = newDate,
            onValueChange = { newDate = it },
            label = { Text("Todo Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val modifiedTodo = todo.copy(name = newName, date = newDate)
            onModify(modifiedTodo)
            onBack()
        }) {
            Text("Modify Todo")
        }
    }
}
