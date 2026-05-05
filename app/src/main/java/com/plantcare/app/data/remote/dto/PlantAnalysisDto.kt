package com.plantcare.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlantAnalysisDto(
    val identification: PlantIdentificationDto,
    val sunlight: SunlightRequirementDto,
    val water: WaterRequirementDto,
    val health: HealthAssessmentDto,
    @SerialName("fun_facts") val funFacts: List<String>,
    @SerialName("care_tips") val careTips: List<String>,
    @SerialName("rag_sources_used") val ragSourcesUsed: List<String> = emptyList()
)

@Serializable
data class PlantIdentificationDto(
    @SerialName("common_name") val commonName: String,
    @SerialName("scientific_name") val scientificName: String,
    val family: String,
    @SerialName("confidence_score") val confidenceScore: Float
)

@Serializable
data class SunlightRequirementDto(
    val level: String,
    @SerialName("hours_per_day") val hoursPerDay: String,
    val tips: String
)

@Serializable
data class WaterRequirementDto(
    val frequency: String,
    val amount: String,
    val tips: String
)

@Serializable
data class HealthAssessmentDto(
    val status: String,
    @SerialName("issues_detected") val issuesDetected: List<String>,
    @SerialName("improvement_tips") val improvementTips: List<String>,
    val urgency: String
)
