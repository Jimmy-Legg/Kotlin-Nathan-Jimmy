package com.example.todolistmultiplatform.android.composable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FilePicker(file: MutableState<String>)
{
    val filePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
    { uri: Uri? ->
        uri?.let { selectedUri -> file.value = selectedUri.toString() }
    }

    TextField(
        value = file.value,
        onValueChange = { },
        label = { Text("Fichier", color = Color.Black) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { filePicker.launch("*/*") },
        readOnly = true,
        enabled = false,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Choisir fichier",
                modifier = Modifier.clickable { filePicker.launch("*/*") },
                tint = Color.Black
            )
        }
    )
}