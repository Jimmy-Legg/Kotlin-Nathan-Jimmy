package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistmultiplatform.android.item.Todo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTodoItemScreen(
    navController: NavHostController,
    todo: Todo,
    onModify: (Todo) -> Unit,
    onBack: () -> Unit
) {
    var newName by remember { mutableStateOf(todo.name) }
    var newDescription by remember { mutableStateOf(todo.description ?: "") }

    val datePickerState = rememberDatePickerState()
    val dateFormatter = SimpleDateFormat(datePattern, Locale.getDefault())

    Column (
        modifier = Modifier.fillMaxSize().padding(8.dp).verticalScroll(rememberScrollState())
    ){
        TopBarApp(navController = navController, pageName = "Modify")
        TextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Nom de la tache") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        DatePicker(
            state = datePickerState,
            modifier = Modifier.fillMaxWidth(),
            dateValidator = { _ -> true },
            colors = DatePickerDefaults.colors()
        )
        Spacer(modifier = Modifier.height(8.dp))
        FloatingActionButton(
            onClick = {
                val selectedDate = datePickerState.selectedDateMillis
                val formattedDate = if (selectedDate != null) dateFormatter.format(Date(selectedDate)) else null
                val modifiedTodo = todo.copy(name = newName, description = newDescription, date = formattedDate)
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
