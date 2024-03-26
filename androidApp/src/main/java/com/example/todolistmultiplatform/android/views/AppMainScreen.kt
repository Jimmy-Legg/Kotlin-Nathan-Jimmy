package com.example.todolistmultiplatform.android.views

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todolistmultiplatform.android.enums.SortOption
import com.example.todolistmultiplatform.android.item.Todo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppMainScreen(
    todoList: List<Todo>,
    filteredTodoList: List<Todo>,
    onNavigateToTaskCreation: () -> Unit,
    onDeleteTask: (Todo) -> Unit,
    onCheckboxClicked: (Todo, Boolean) -> Unit,
    sortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onModifyTodo: (Todo) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = contentColorFor(backgroundColor)
    val squareShape: Shape = RoundedCornerShape(size = 10.dp)

    val unfinishedTasksCount = todoList.count { !it.isDone }

    val context = LocalContext.current
    checkForOverdueTasksAndNotify(context, todoList)

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(modifier = Modifier.padding(16.dp)) {
            SortDropdownMenu(
                sortOption = sortOption,
                onSortOptionSelected = onSortOptionSelected,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )
            Box(modifier = Modifier.weight(1f)) {
                TodoList(
                    todoList = filteredTodoList,
                    onDelete = onDeleteTask,
                    onCheckboxClicked = onCheckboxClicked,
                    onModify = onModifyTodo
                )
            }
            Box(modifier = Modifier.height(IntrinsicSize.Min)) {
                Column {
                    Text(
                        text = "Unfinished tasks: $unfinishedTasksCount",
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )

                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { onNavigateToTaskCreation() },
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                shape = squareShape,
                contentColor = contentColor,
                content = { Icon(Icons.Filled.Add, contentDescription = "Add Todo") }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ServiceCast")
fun checkForOverdueTasksAndNotify(context: Context, todoList: List<Todo>) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Create the notification channel
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Todo Notifications"
        val descriptionText = "Channel for todo notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("todo_channel", name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    // Check for overdue tasks
    val currentDate = LocalDate.now()

    todoList.forEach { todo ->
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val todoDate = LocalDate.parse(todo.date, formatter)

            // Compare todo date to current date
            if (todoDate.isBefore(currentDate) || todoDate == currentDate) {
                // Todo item is overdue or due today
                val daysUntilDue = ChronoUnit.DAYS.between(currentDate, todoDate).toInt()
                val notificationMessage = when {
                    daysUntilDue < 0 -> "Overdue by ${-daysUntilDue} days"
                    daysUntilDue == 0 -> "Due today"
                    else -> "Due in $daysUntilDue days"
                }

                // Send notification
                val notification = Notification.Builder(context, "todo_channel")
                    .setContentTitle("Task Due Soon")
                    .setContentText("${todo.name}: $notificationMessage")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .build()
                notificationManager.notify(todo.id, notification)
            } else if (todoDate.minusDays(1) == currentDate) {
                // Todo item is due in one day
                val notification = Notification.Builder(context, "todo_channel")
                    .setContentTitle("Task Due Soon")
                    .setContentText("${todo.name}: Due in 1 day")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .build()
                notificationManager.notify(todo.id, notification)
            }
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
        }
    }
}
