package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TaskCreationScreen(navController: NavHostController, onTaskCreated: (name : String, date : String) -> Unit) {
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
        //Task name
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Task Name") },
            isError = !isNameValid.value,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Needs to be replaced
        TextField(
            value = date.value,
            onValueChange = { date.value = it },
            label = { Text("Task Date (YYYY-MM-DD)") },
            isError = !isDateValid.value,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Validation Button
        FloatingActionButton(
            onClick = {
                if (validateInputs()) {
                    onTaskCreated(name.value, date.value)
                    navController.navigate("task_list") {
                        popUpTo("task_creation") {
                            inclusive = true
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.inversePrimary,
            content = {
                Text("Create Task", color = MaterialTheme.colorScheme.primary)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Back to Main Screen Button
        FloatingActionButton(
            onClick = {
                navController.navigate("task_list")
            },
            modifier = Modifier.fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.inversePrimary,
            content = {
                Text("Back to Main Screen", color = MaterialTheme.colorScheme.primary)
            }
        )
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
