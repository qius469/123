
package com.example.geminiapp.data

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content,
    val finishReason: String
)

data class ApiError(
    val error: Error
)

data class Error(
    val code: Int,
    val message: String,
    val status: String
)
