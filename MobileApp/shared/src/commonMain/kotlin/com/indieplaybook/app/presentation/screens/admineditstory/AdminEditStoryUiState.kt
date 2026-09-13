package com.indieplaybook.app.presentation.screens.admineditstory

import com.indieplaybook.app.domain.model.AppStory

data class AdminEditStoryUiState(
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
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSavedSuccessfully: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface AdminEditStoryUiEvent {
    data class OnNameChanged(val value: String) : AdminEditStoryUiEvent
    data class OnOneLinerChanged(val value: String) : AdminEditStoryUiEvent
    data class OnTechStackChanged(val value: String) : AdminEditStoryUiEvent
    data class OnOriginStoryChanged(val value: String) : AdminEditStoryUiEvent
    data class OnGrowthPlaybookChanged(val value: String) : AdminEditStoryUiEvent
    data class OnCategoryChanged(val value: String) : AdminEditStoryUiEvent
    data class OnPublisherChanged(val value: String) : AdminEditStoryUiEvent
    data class OnRevenueChanged(val value: String) : AdminEditStoryUiEvent
    data class OnDownloadsChanged(val value: String) : AdminEditStoryUiEvent
    data object OnSaveClicked : AdminEditStoryUiEvent
}
