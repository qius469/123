
package com.example.geminiapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminiapp.data.Content
import com.example.geminiapp.data.GeminiRequest
import com.example.geminiapp.data.Part
import com.example.geminiapp.network.ApiClient
import kotlinx.coroutines.launch

sealed class UiState {
    object Initial : UiState()
    object Loading : UiState()
    data class Success(val response: String) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel : ViewModel() {
    var uiState: UiState by mutableStateOf(UiState.Initial)
        private set

    var userInput: String by mutableStateOf("")
        private set

    // 在实际应用中，应该从安全的配置或后端获取
    private val API_KEY = "YOUR_API_KEY"

    fun onUserInputChange(input: String) {
        userInput = input
    }

    fun sendMessage() {
        if (userInput.isBlank()) return

        viewModelScope.launch {
            try {
                uiState = UiState.Loading
                
                val request = GeminiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(
                                Part(text = userInput)
                            )
                        )
                    )
                )

                val response = ApiClient.geminiService.generateContent(
                    apiKey = "Bearer $API_KEY",
                    request = request
                )

                val responseText = response.candidates.firstOrNull()
                    ?.content?.parts?.firstOrNull()?.text
                    ?: throw Exception("No response from API")

                uiState = UiState.Success(responseText)
            } catch (e: Exception) {
                uiState = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
