
import android.content.Context
import com.example.todolistmultiplatform.android.item.Todo
import com.google.gson.Gson
import java.io.IOException
import java.io.OutputStream

object JsonUtils {

    private var defaultJson = "[]"
    private val gson = Gson()

    fun saveTodoList(context: Context, todoList: List<Todo>)
    {
        try {
            val jsonData = gson.toJson(todoList)
            val outputStream: OutputStream = context.openFileOutput("todo_list.json", Context.MODE_PRIVATE)
            outputStream.write(jsonData.toByteArray())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadFile(context: Context): List<Todo> {
        return try
        {
            val inputStream = context.openFileInput("todo_list.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            parseTodoList(String(buffer, Charsets.UTF_8))
        }
        catch (e: IOException)
        {
            e.printStackTrace()
            createDefaultFile(context)
            emptyList<Todo>()
        }
    }

    fun createDefaultFile(context: Context)
    {
        try {
            val outputStream: OutputStream = context.openFileOutput("todo_list.json", Context.MODE_PRIVATE)
            outputStream.write(defaultJson.toByteArray())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parseTodoList(json: String): List<Todo>
    {
        val listType = object : com.google.gson.reflect.TypeToken<List<Todo>>() {}.type
        return gson.fromJson(json, listType)
    }
}
