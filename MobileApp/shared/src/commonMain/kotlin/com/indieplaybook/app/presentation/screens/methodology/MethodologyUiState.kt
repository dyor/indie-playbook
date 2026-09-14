package com.indieplaybook.app.presentation.screens.methodology

class MethodologyUiState

sealed interface MethodologyUiEvent {
    data object OnClick : MethodologyUiEvent
}
