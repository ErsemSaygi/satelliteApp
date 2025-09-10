package com.example.satelliteapp.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import java.text.SimpleDateFormat
import java.util.Locale

fun setBoldSpannable(text: String): SpannableString {
    val spannable = SpannableString(text)
    val length = text.indexOf(":") + 1
    spannable.setSpan(StyleSpan(Typeface.BOLD),0,length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    return spannable
}

fun convertTime(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    val inputDate = inputFormat.parse(date)
    return inputDate?.let { outputFormat.format(it) }.toString()
}