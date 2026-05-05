package com.plantcare.app.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.plantcare.app.data.model.HealthAssessment
import com.plantcare.app.data.model.PlantAnalysis
import com.plantcare.app.data.model.PlantIdentification
import com.plantcare.app.data.model.SunlightRequirement
import com.plantcare.app.data.model.UrgencyLevel
import com.plantcare.app.data.model.WaterRequirement
import com.plantcare.app.data.remote.PlantApiService
import com.plantcare.app.data.remote.dto.AnalyzeRequest
import com.plantcare.app.data.remote.dto.PlantAnalysisDto
import java.io.ByteArrayOutputStream
import android.util.Base64
import javax.inject.Inject

class PlantRepository @Inject constructor(
    private val apiService: PlantApiService
) {
    suspend fun analyzeImage(context: Context, uri: Uri, userNote: String? = null): Result<PlantAnalysis> {
        return runCatching {
            val base64 = uri.toCompressedBase64(context)
            val dto = apiService.analyzePlant(AnalyzeRequest(imageBase64 = base64, userNote = userNote))
            dto.toDomain()
        }
    }
}

private fun Uri.toCompressedBase64(context: Context): String {
    val inputStream = context.contentResolver.openInputStream(this)
        ?: throw IllegalArgumentException("Cannot open image URI")

    val original = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    // Scale down to max 1024px on longest side
    val maxPx = 1024
    val scaled = if (original.width > maxPx || original.height > maxPx) {
        val ratio = minOf(maxPx.toFloat() / original.width, maxPx.toFloat() / original.height)
        val newWidth = (original.width * ratio).toInt()
        val newHeight = (original.height * ratio).toInt()
        Bitmap.createScaledBitmap(original, newWidth, newHeight, true)
    } else {
        original
    }

    val buffer = ByteArrayOutputStream()
    scaled.compress(Bitmap.CompressFormat.JPEG, 85, buffer)
    return Base64.encodeToString(buffer.toByteArray(), Base64.NO_WRAP)
}

private fun PlantAnalysisDto.toDomain(): PlantAnalysis = PlantAnalysis(
    identification = PlantIdentification(
        commonName = identification.commonName,
        scientificName = identification.scientificName,
        family = identification.family,
        confidenceScore = identification.confidenceScore
    ),
    sunlight = SunlightRequirement(
        level = sunlight.level,
        hoursPerDay = sunlight.hoursPerDay,
        tips = sunlight.tips
    ),
    water = WaterRequirement(
        frequency = water.frequency,
        amount = water.amount,
        tips = water.tips
    ),
    health = HealthAssessment(
        status = health.status,
        issuesDetected = health.issuesDetected,
        improvementTips = health.improvementTips,
        urgency = UrgencyLevel.fromString(health.urgency)
    ),
    funFacts = funFacts,
    careTips = careTips,
    ragSourcesUsed = ragSourcesUsed
)
