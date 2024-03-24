package com.example.todolistmultiplatform.android.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistmultiplatform.android.enums.SortOption
import com.example.todolistmultiplatform.android.item.Todo
import kotlin.math.abs


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppMainScreen(
    todoList: List<Todo>,
    onNavigateToTaskCreation: () -> Unit,
    onDeleteTask: (Todo) -> Unit,
    onCheckboxClicked: (Todo, Boolean) -> Unit,
    sortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = contentColorFor(backgroundColor)

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(modifier = Modifier.padding(16.dp)) {
            SortDropdownMenu(
                sortOption = sortOption,
                onSortOptionSelected = onSortOptionSelected,
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDropdownMenu(
    sortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = sortOption.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Sort by") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.name) },
                    onClick = {
                        onSortOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoItem(
    todo: Todo,
    onDelete: () -> Unit,
    onCheckboxClicked: (Boolean) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isSwiped by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }

    val slidingThreshold = 50.dp
    val startThreshold = 5.dp

    val overdue = todo.isOverdue()
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .offset(offsetX.dp, 0.dp)
            ) {
                Column(modifier = Modifier
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectDragGestures(onDragStart = {
                            // Reset offsetX when starting to slide
                            offsetX = 0f
                        }, onDragEnd = {
                            if (abs(offsetX) > slidingThreshold.value) {
                                isConfirmVisible = true
                            }
                            offsetX = 0f
                            isSwiped = false
                        }) { _, dragAmount ->
                            if (abs(dragAmount.x) > startThreshold.value) {
                                offsetX += dragAmount.x
                                isSwiped = offsetX != 0f
                            }
                        }
                    }
                ) {
                    Text(text = todo.name, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = todo.date ?: "No Date",
                        fontSize = 14.sp,
                        color = if (overdue) Color.Red else Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when {
                            overdue && !todo.isDone -> "Overdue"
                            todo.isDone -> "Done"
                            else -> "Not Done"
                        },
                        color = when {
                            overdue && !todo.isDone -> Color.Red
                            todo.isDone -> Color.Green
                            else -> Color.Black
                        },
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Checkbox(
                    checked = todo.isDone,
                    onCheckedChange = { isChecked ->
                        onCheckboxClicked(isChecked)
                    },
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }

        if (isSwiped) {
            val iconModifier = Modifier
                .padding(16.dp)
                .align(if (offsetX > 0) Alignment.CenterStart else Alignment.CenterEnd)

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = iconModifier
            )
        }

        if (isConfirmVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { /* do nothing to prevent clicks from passing through */ },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(25.dp)
                ) {
                    Button(
                        onClick = {
                            onDelete()
                            isConfirmVisible = false
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            isConfirmVisible = false
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}
