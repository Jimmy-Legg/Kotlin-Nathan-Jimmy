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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.todolistmultiplatform.android.item.Todo
import kotlin.math.abs


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoItem(
    todo: Todo,
    onDelete: () -> Unit,
    onCheckboxClicked: (Boolean) -> Unit,
    onModify: () -> Unit
) {
    val slidingSpeedFactor = 0.3f
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isSwiped by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }

    val slidingThreshold = 75.dp
    val startThreshold = 0.dp

    val overdue = todo.isOverdue()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onModify() }
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
                        }) { change, dragAmount ->
                            val horizontalDrag = abs(dragAmount.x)
                            val verticalDrag = abs(dragAmount.y)

                            // Only consider horizontal drag
                            if (horizontalDrag > verticalDrag && horizontalDrag > startThreshold.value) {
                                offsetX += dragAmount.x * slidingSpeedFactor // Adjust sliding speed
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

            val iconColor = if (abs(offsetX) > slidingThreshold.value) Color.Red else Color.Black

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = iconColor,
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