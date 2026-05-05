package com.plantcare.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeRequest(
    @SerialName("image_base64") val imageBase64: String,
    @SerialName("user_note") val userNote: String? = null
)
