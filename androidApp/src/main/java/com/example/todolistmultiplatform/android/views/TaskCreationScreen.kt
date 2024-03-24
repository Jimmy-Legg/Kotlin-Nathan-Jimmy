
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

var datePattern = "dd/MM/yyyy"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationScreen(
    navController: NavHostController,
    onTaskCreated: (name: String, date: String) -> Unit
) {

    val name = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }

    val isNameValid = remember { mutableStateOf(true) }
    val isDateValid = remember { mutableStateOf(true) }

    fun validateInputs(): Boolean {
        isNameValid.value = name.value.isNotBlank()
        isDateValid.value = isValidDate(date.value)

        return isNameValid.value && isDateValid.value
    }

    val datePickerState = rememberDatePickerState()
    val dateFormatter = DateTimeFormatter.ofPattern(datePattern)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Task Name") },
            isError = !isNameValid.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DatePicker(
            state = datePickerState,
            modifier = Modifier.fillMaxWidth(),
            dateValidator = { _ -> true },
            colors = DatePickerDefaults.colors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FloatingActionButton(
            onClick = {
                val selectedDate = datePickerState.selectedDateMillis
                if(selectedDate != null)
                {
                    val formatter = SimpleDateFormat(datePattern, Locale.getDefault())
                    val formattedDate = formatter.format(Date(selectedDate))

                    date.value = formattedDate

                    if (validateInputs())
                    {
                        onTaskCreated(name.value, formattedDate)
                        navController.navigate("task_list") {
                            popUpTo("task_creation") {
                                inclusive = true
                            }
                        }
                    } else {
                        Log.d("TaskCreationScreen", "Create Task button clicked but inputs are invalid or date is null")
                        Log.d("Date", formattedDate)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.inversePrimary,
            content = {
                Text("Create Task", color = MaterialTheme.colorScheme.primary)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        FloatingActionButton(
            onClick = {
                navController.navigate("task_list")
            },
            modifier = Modifier.fillMaxWidth(),
            contentColor = MaterialTheme.colorScheme.inversePrimary,
            content = {
                Text("Back to Main Screen", color = MaterialTheme.colorScheme.primary)
            }
        )
    }
}

fun isValidDate(dateString: String): Boolean {
    return try {
        val formatter = SimpleDateFormat(datePattern, Locale.getDefault())
        formatter.parse(dateString)
        true
    } catch (e: ParseException) {
        false
    }
}
