package com.example.todolistmultiplatform.android.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCreationScreen(
    navController: NavHostController,
    onTaskCreated: (name: String,description: String?, date: String?, file: String?) -> Unit
) {

    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val file = remember { mutableStateOf("") }

    val isNameValid = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TopBarApp(navController = navController, pageName = "Nouvelle tache")

        TaskFormItem(name, description, date, file, isNameValid)

        Spacer(modifier = Modifier.height(8.dp))

        FloatingActionButton(
            onClick = {
                if (isNameValid.value)
                {
                    val descriptionToUse = description.value.ifBlank { null }
                    val fileToUse = file.value.ifBlank { null }

                    onTaskCreated(name.value, descriptionToUse, date.value, fileToUse)
                    navController.navigate("task_list")
                    {
                        popUpTo("task_creation")
                        {
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
    }
}

