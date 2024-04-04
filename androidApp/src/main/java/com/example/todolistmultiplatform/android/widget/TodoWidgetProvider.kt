package com.example.todolistmultiplatform.android.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.example.todolistmultiplatform.android.R

class TodoWidgetProvider : AppWidgetProvider() {
    companion object {
        const val ACTION_UPDATE_WIDGET = "com.example.todolistmultiplatform.android.widget.ACTION_UPDATE_WIDGET"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val todoList = JsonUtils.loadFile(context)
        val filteredTodoList = todoList.filter { it.date != "" }.sortedBy { it.date }

        val views = RemoteViews(context.packageName, R.layout.todo_widget)

        views.removeAllViews(R.id.container)

        filteredTodoList.take(3).forEach { todo ->
            val todoView = RemoteViews(context.packageName, R.layout.todo_item)
            todoView.setTextViewText(R.id.name_text, todo.name)

            if (!todo.description.isNullOrEmpty()) {
                todoView.setTextViewText(R.id.description_text, "Description: ${todo.description}")
                todoView.setViewVisibility(R.id.description_text, View.VISIBLE)
            }

            todoView.setTextViewText(R.id.date_text, "Date: ${todo.date}")
            todoView.setViewVisibility(R.id.date_text, View.VISIBLE)


            todoView.setTextViewText(R.id.is_done_text, "Is Done: ${todo.isDone}")

            views.addView(R.id.container, todoView)
        }

        val intent = Intent(context, TodoWidgetProvider::class.java)
        intent.action = ACTION_UPDATE_WIDGET
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.update_button, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }







    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_UPDATE_WIDGET) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, TodoWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            appWidgetIds.forEach { appWidgetId ->
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        } else {
            super.onReceive(context, intent)
        }
    }
}
