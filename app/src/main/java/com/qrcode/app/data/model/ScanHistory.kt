package com.qrcode.app.data.model

import java.util.Date

data class ScanHistory(
    val id: Long,
    val content: String,
    val type: String, // "QR Code" or "Barcode"
    val timestamp: Date,
    val isFavorite: Boolean = false
)