package com.indieplaybook.app.presentation.screens.suggestedits

import com.indieplaybook.app.domain.model.AppStory

data class SuggestEditsUiState(
    val storyId: String = "",
    val originalStory: AppStory? = null,
    val name: String = "",
    val oneLiner: String = "",
    val techStack: String = "",
    val originStory: String = "",
    val growthPlaybook: String = "",
    val category: String = "",
    val publisher: String = "",
    val revenue: String = "",
    val downloads: String = "",
    val note: String = "",
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val isSubmittedSuccessfully: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface SuggestEditsUiEvent {
    data class OnNameChanged(val value: String) : SuggestEditsUiEvent
    data class OnOneLinerChanged(val value: String) : SuggestEditsUiEvent
    data class OnTechStackChanged(val value: String) : SuggestEditsUiEvent
    data class OnOriginStoryChanged(val value: String) : SuggestEditsUiEvent
    data class OnGrowthPlaybookChanged(val value: String) : SuggestEditsUiEvent
    data class OnCategoryChanged(val value: String) : SuggestEditsUiEvent
    data class OnPublisherChanged(val value: String) : SuggestEditsUiEvent
    data class OnRevenueChanged(val value: String) : SuggestEditsUiEvent
    data class OnDownloadsChanged(val value: String) : SuggestEditsUiEvent
    data class OnNoteChanged(val value: String) : SuggestEditsUiEvent
    data object OnSubmitClicked : SuggestEditsUiEvent
}
