package com.plantcare.app.data.remote

import com.plantcare.app.data.remote.dto.AnalyzeRequest
import com.plantcare.app.data.remote.dto.PlantAnalysisDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PlantApiService {
    @POST("api/v1/analyze")
    suspend fun analyzePlant(@Body request: AnalyzeRequest): PlantAnalysisDto
}
