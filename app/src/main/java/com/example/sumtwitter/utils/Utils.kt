package com.example.sumtwitter.utils

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

fun showText(context: Context, text: String) =
    makeText(context, text, Toast.LENGTH_LONG).show()
