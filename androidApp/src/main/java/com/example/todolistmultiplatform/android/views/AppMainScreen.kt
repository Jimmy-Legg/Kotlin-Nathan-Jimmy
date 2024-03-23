package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolistmultiplatform.android.item.Todo
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun AppMainScreen(
    todoList: List<Todo>,
    onNavigateToTaskCreation: () -> Unit,
    onDeleteTask: (Todo) -> Unit,
    onCheckboxClicked: (Todo, Boolean) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = contentColorFor(backgroundColor)

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(modifier = Modifier.padding(16.dp)) {
            Greeting(modifier = Modifier.padding(bottom = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.weight(1f)) {
                TodoList(
                    todoList = todoList,
                    onDelete = onDeleteTask,
                    onCheckboxClicked = onCheckboxClicked
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            FloatingActionButton(
                onClick = { onNavigateToTaskCreation() },
                modifier = Modifier.fillMaxWidth(),
                contentColor = contentColor,
                content = { Icon(Icons.Default.Add, contentDescription = "Add Todo") }
            )
        }
    }
}


@Composable
fun TodoList(todoList: List<Todo>, onDelete: (Todo) -> Unit, onCheckboxClicked: (Todo, Boolean) -> Unit) {
    LazyColumn {
        items(todoList) { todo ->
            TodoItem(
                todo = todo,
                onDelete = { onDelete(todo) },
                onCheckboxClicked = { isChecked -> onCheckboxClicked(todo, isChecked) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onDelete: () -> Unit, onCheckboxClicked: (Boolean) -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = todo.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = todo.date, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (todo.isDone) "Done" else "Not Done",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (todo.isDone) Color.Green else Color.Red
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { isChecked ->
                    onCheckboxClicked(isChecked)
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            FloatingActionButton(
                onClick = onDelete,
                modifier = Modifier.size(60.dp), // Set a smaller size for the button
                content = {
                    Text(text = "Delete")
                }
            )
        }
    }
}


@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Hello!",
        modifier = modifier
    )
}