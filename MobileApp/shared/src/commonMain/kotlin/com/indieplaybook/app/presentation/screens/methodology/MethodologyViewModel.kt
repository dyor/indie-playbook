package com.indieplaybook.app.presentation.screens.methodology

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MethodologyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MethodologyUiState())
    val uiState: StateFlow<MethodologyUiState> = _uiState.asStateFlow()

    fun onUiEvent(event: MethodologyUiEvent) {
        when (event) {
            MethodologyUiEvent.OnClick -> TODO()
        }
    }
}
