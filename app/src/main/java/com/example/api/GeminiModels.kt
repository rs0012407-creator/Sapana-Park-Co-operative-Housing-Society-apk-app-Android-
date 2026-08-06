package com.example.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @field:Json(name = "contents") val contents: List<GeminiContent>,
    @field:Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @field:Json(name = "parts") val parts: List<GeminiPart>,
    @field:Json(name = "role") val role: String? = "user"
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @field:Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @field:Json(name = "temperature") val temperature: Float = 0.2f,
    @field:Json(name = "topK") val topK: Int = 40,
    @field:Json(name = "topP") val topP: Float = 0.95f,
    @field:Json(name = "maxOutputTokens") val maxOutputTokens: Int = 1024
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @field:Json(name = "candidates") val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @field:Json(name = "content") val content: GeminiContent? = null,
    @field:Json(name = "finishReason") val finishReason: String? = null
)

data class AiTroubleshootResult(
    val explanation: String,
    val recommendedFixAction: String, // "FIX_COMPLAINT_STATUS", "REGENERATE_RECEIPT", "SYNC_PROFILE_DATA", "CLEAR_DUES_SYNC", "NONE"
    val isAutoFixed: Boolean = false
)

data class AiTroubleshootUiState(
    val isLoading: Boolean = false,
    val result: AiTroubleshootResult? = null,
    val error: String? = null,
    val lastFixMessage: String? = null
)
