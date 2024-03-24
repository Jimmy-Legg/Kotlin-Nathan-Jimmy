package com.example.todolistmultiplatform.android.utils

import com.example.todolistmultiplatform.android.item.Todo

fun getFreeId(list: List<Todo>): Int {
    var freeId = 0
    while (freeId in list.map { it.id }) {
        freeId++
    }
    return freeId
}

