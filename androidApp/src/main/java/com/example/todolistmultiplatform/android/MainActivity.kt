package com.example.todolistmultiplatform.android

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todolistmultiplatform.Greeting
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoListContent()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}

data class Task(
    val name: String,
    val isDone: Boolean
)

private const val TASKS_PREF_KEY = "tasks"

fun saveTasksToPrefs(context: Context, tasks: List<Task>) {
    val prefs = context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    val json = Gson().toJson(tasks)
    prefs.edit().putString(TASKS_PREF_KEY, json).apply()
    Log.d("SaveTasks", "Tasks saved: $tasks")
}

fun loadTasksFromPrefs(context: Context): List<Task> {
    val prefs = context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    val json = prefs.getString(TASKS_PREF_KEY, null)
    return if (json != null) {
        Gson().fromJson(json, object : TypeToken<List<Task>>() {}.type)
    } else {
        emptyList()
    }
}

@Composable
fun TodoListContent() {

    val context = LocalContext.current
    var tasks by remember { mutableStateOf(loadTasksFromPrefs(context)) }
    Log.d("LoadTasks", "Tasks loaded: $tasks")
    var showDialog by remember { mutableStateOf(false) }
    var newTaskName by remember { mutableStateOf(TextFieldValue()) }
    var selectedTask by remember { mutableStateOf(Task("", false)) }
    var showTaskDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            saveTasksToPrefs(context, tasks)
            Log.d("DisposableEffect", "Tasks saved on dispose: $tasks")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TodoList",
                style = MaterialTheme.typography.h4(),
                modifier = Modifier.padding(vertical = 16.dp)
            )
            TodoListView(
                tasks = tasks,
                onTaskClicked = { task ->
                    selectedTask = task
                    showTaskDialog = true
                },
                onTaskChecked = { task, isChecked ->
                    tasks = tasks.map {
                        if (it == task) {
                            it.copy(isDone = isChecked)
                        } else {
                            it
                        }
                    }
                }
            )
        }
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd), // Align to bottom end (right corner)
            content = {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        )
        if (showDialog) {
            AddTaskDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    tasks = tasks.toMutableList().apply {
                        add(Task(newTaskName.text, false))
                    }
                    showDialog = false
                    newTaskName = TextFieldValue()
                },
                newTaskName = newTaskName,
                onNameChange = { newTaskName = it }
            )
        }
        if (showTaskDialog) {
            TaskDialog(
                task = selectedTask.name,
                onDismiss = { showTaskDialog = false },
                onDelete = {
                    tasks = tasks.filter { it != selectedTask }
                    showTaskDialog = false
                },
                onModify = { modifiedTask ->
                    tasks = tasks.map { (if (it == selectedTask) modifiedTask else it) as Task }
                }
            )
        }
    }
}

@Composable
fun TodoListView(tasks: List<Task>, onTaskClicked: (Task) -> Unit, onTaskChecked: (Task, Boolean) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(tasks.size) { index ->
            TodoItem(
                task = tasks[index],
                onClick = { onTaskClicked(tasks[index]) },
                onCheckedChange = { isChecked ->
                    onTaskChecked(tasks[index], isChecked)
                }
            )
        }
    }
}

@Composable
fun TodoItem(task: Task, onClick: () -> Unit, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .fillMaxWidth(), // Make the row fill the width of the screen
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isDone,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = task.name)
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    newTaskName: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = newTaskName,
                    onValueChange = { onNameChange(it) },
                    label = { Text("Task Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onConfirm) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun TaskDialog(
    task: String,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onModify: (String) -> Unit
) {
    var newTaskName by remember { mutableStateOf(TextFieldValue(task)) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = newTaskName,
                    onValueChange = { newTaskName = it },
                    label = { Text("Task Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDelete) {
                        Text("Delete")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onModify(newTaskName.text)
                        onDismiss()
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        TodoListContent()
    }
}

fun Typography.h4(): TextStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp
)
