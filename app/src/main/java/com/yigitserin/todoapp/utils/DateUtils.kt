package com.yigitserin.todoapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.prettyDate(): String{
    val simpleDateFormat= SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return simpleDateFormat.format(this)
}