package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.todolistmultiplatform.android.item.Todo

@Composable
fun AppMainScreen(todoList: List<Todo>, onNavigateToTaskCreation: () -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = contentColorFor(backgroundColor)

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Box {
            Column {
                Greeting()
                TodoList(todoList)
            }

            val newTodoList = remember { mutableStateOf(todoList) }

            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = {
                    onNavigateToTaskCreation()
                },
                containerColor = Color.Green,
                contentColor = contentColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    }
}




@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Hello !",
        modifier = modifier
    )
}

@Composable
fun TodoList(todoList: List<Todo>) {
    Column {
        todoList.forEach { todo ->
            Text(text = todo.name)
            Text(text = todo.date)
            Text(text = if (todo.isDone) "Done" else "Not Done")
        }
    }
}


