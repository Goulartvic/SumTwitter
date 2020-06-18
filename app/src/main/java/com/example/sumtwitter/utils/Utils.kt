package com.example.sumtwitter.utils

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

fun showText(context: Context, text: String) =
    makeText(context, text, Toast.LENGTH_LONG).show()

fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}