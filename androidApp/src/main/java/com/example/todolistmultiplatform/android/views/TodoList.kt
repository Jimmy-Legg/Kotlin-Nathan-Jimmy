package com.example.todolistmultiplatform.android.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolistmultiplatform.android.item.Todo

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoList(
    todoList: List<Todo>,
    onDelete: (Todo) -> Unit,
    onCheckboxClicked: (Todo, Boolean) -> Unit,
    onModify: (Todo) -> Unit
) {
    LazyColumn {
        items(todoList) { todo ->
            TodoItem(
                todo = todo,
                onDelete = { onDelete(todo) },
                onCheckboxClicked = { isChecked -> onCheckboxClicked(todo, isChecked) },
                onModify = { onModify(todo) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}