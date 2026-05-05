package com.plantcare.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlantAnalysis(
    val identification: PlantIdentification,
    val sunlight: SunlightRequirement,
    val water: WaterRequirement,
    val health: HealthAssessment,
    val funFacts: List<String>,
    val careTips: List<String>,
    val ragSourcesUsed: List<String>
)

@Serializable
data class PlantIdentification(
    val commonName: String,
    val scientificName: String,
    val family: String,
    val confidenceScore: Float
)

@Serializable
data class SunlightRequirement(
    val level: String,
    val hoursPerDay: String,
    val tips: String
)

@Serializable
data class WaterRequirement(
    val frequency: String,
    val amount: String,
    val tips: String
)

@Serializable
data class HealthAssessment(
    val status: String,
    val issuesDetected: List<String>,
    val improvementTips: List<String>,
    val urgency: UrgencyLevel
)

enum class UrgencyLevel(val displayLabel: String) {
    LOW("Healthy"),
    MEDIUM("Needs Attention"),
    HIGH("Act Soon"),
    CRITICAL("Critical");

    companion object {
        fun fromString(value: String): UrgencyLevel = when (value.lowercase()) {
            "medium" -> MEDIUM
            "high" -> HIGH
            "critical" -> CRITICAL
            else -> LOW
        }
    }
}
