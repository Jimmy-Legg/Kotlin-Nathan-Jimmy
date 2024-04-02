package com.example.todolistmultiplatform.android.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskFormItem(
    name: MutableState<String>,
    description:  MutableState<String>,
    date: MutableState<String>,
    file:  MutableState<String>,
    isNameValid:  MutableState<Boolean>
)
{
    fun validateInputs(): Boolean {
        isNameValid.value = name.value.isNotBlank()
        return isNameValid.value
    }

    TextField(
        value = name.value,
        onValueChange = { name.value = it; validateInputs()},
        label = { Text("Nom") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        value = description.value,
        onValueChange = { description.value = it },
        label = { Text("Description") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    DateInput(date)

    Spacer(modifier = Modifier.height(8.dp))

    FilePicker(file)
}