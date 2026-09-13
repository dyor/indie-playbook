package com.indieplaybook.app.presentation.screens.adminreviewsuggestions

import com.indieplaybook.app.domain.model.AppStory
import com.indieplaybook.app.domain.model.AppStorySuggestion

data class SuggestionItemUiState(
    val suggestion: AppStorySuggestion,
    val story: AppStory?,
)

data class AdminReviewSuggestionsUiState(
    val suggestions: List<SuggestionItemUiState> = emptyList(),
    val isLoading: Boolean = true,
    val isActionInProgress: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

sealed interface AdminReviewSuggestionsUiEvent {
    data class OnApproveClicked(val suggestion: AppStorySuggestion, val story: AppStory) : AdminReviewSuggestionsUiEvent
    data class OnRejectClicked(val suggestionId: String) : AdminReviewSuggestionsUiEvent
    data object OnRefresh : AdminReviewSuggestionsUiEvent
}
