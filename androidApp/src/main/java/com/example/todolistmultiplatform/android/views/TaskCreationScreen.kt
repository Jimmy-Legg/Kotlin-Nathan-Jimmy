package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistmultiplatform.android.item.Todo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskCreationScreen(navController: NavHostController, onTaskCreated: (Todo) -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = contentColorFor(backgroundColor)

    val name = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }

    val isNameValid = remember { mutableStateOf(true) }
    val isDateValid = remember { mutableStateOf(true) }

    fun validateInputs(): Boolean {
        isNameValid.value = name.value.isNotBlank()
        isDateValid.value = isValidDate(date.value)

        return isNameValid.value && isDateValid.value
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Task Name") },
            isError = !isNameValid.value,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = date.value,
            onValueChange = { date.value = it },
            label = { Text("Due Date (yyyy-MM-dd)") },
            isError = !isDateValid.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (validateInputs()) {
                    onTaskCreated(Todo(name.value, date.value, false))
                    navController.navigate("task_list") {
                        popUpTo("task_creation") {
                            inclusive = true
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Task")
        }
    }
}

fun isValidDate(dateString: String): Boolean {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.parse(dateString)
        true
    } catch (e: ParseException) {
        false
    }
}
