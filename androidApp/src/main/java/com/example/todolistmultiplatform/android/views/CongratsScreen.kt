package com.example.todolistmultiplatform.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistmultiplatform.android.item.Todo
import com.example.todolistmultiplatform.android.particles.ControlledExplosion


@Composable
fun CongratsScreen(
    todo: Todo,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ControlledExplosion()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Bravo !", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Vous avez terminé la tâche :", fontSize = 18.sp)
            Text(text = todo.name, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBackPressed) {
                Text(text = "retour")
            }
        }
    }
}