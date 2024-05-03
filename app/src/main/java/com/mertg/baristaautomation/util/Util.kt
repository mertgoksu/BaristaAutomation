package com.mertg.baristaautomation.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

// Sipariş tarihini biçimlendirmek için bir fonksiyon
fun formatDate(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(date)
}

fun generateOrderId(): String {
    val length = 8
    val allowedChars = "1234567890"
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}