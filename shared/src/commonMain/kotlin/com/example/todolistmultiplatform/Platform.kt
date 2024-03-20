package com.example.todolistmultiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform