package com.example.todolistmultiplatform.android.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

var datePattern = "dd/MM/yyyy"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(date: MutableState<String>)
{
    var showDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val dateFormatter = SimpleDateFormat(datePattern, Locale.getDefault())

    TextField(
        value = date.value,
        onValueChange = { date.value = it },
        label = { Text("Date", color = Color.Black) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        readOnly = true,
        enabled = false,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Selection de date",
                modifier = Modifier.clickable { showDialog = true },
                tint = Color.Black,
            )
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true, dismissOnClickOutside = true),
            title = { Text("Select a date") },
            text = {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.fillMaxWidth(),
                    dateValidator = { _ -> true },
                    colors = DatePickerDefaults.colors()
                )
            },
            confirmButton = {
                Button(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    val formattedDate = if (selectedDate != null) dateFormatter.format(Date(selectedDate)) else null
                    date.value = formattedDate ?: ""
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}