package com.indieplaybook.app.presentation.screens.adminreviewsuggestions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indieplaybook.app.designsystem.components.AppButton
import com.indieplaybook.app.designsystem.components.EmptyContentView
import com.indieplaybook.app.designsystem.components.LoadingProgress
import com.indieplaybook.app.designsystem.components.LoadingProgressMode
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_back
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.domain.model.AppStory
import com.indieplaybook.app.domain.model.AppStorySuggestion
import com.indieplaybook.app.util.diff.TextDiff

@Composable
fun AdminReviewSuggestionsScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminReviewSuggestionsViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminReviewSuggestionsScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun AdminReviewSuggestionsScreen(
    modifier: Modifier = Modifier,
    uiState: AdminReviewSuggestionsUiState,
    onUiEvent: (AdminReviewSuggestionsUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = "Pending Suggestions",
        onNavigationIconClick = onNavigateBack,
        navigationIcon = UiRes.drawable.ic_back,
        includeBottomInsets = true,
    ) {
        if (uiState.isLoading) {
            LoadingProgress(mode = LoadingProgressMode.FULLSCREEN)
        } else if (uiState.suggestions.isEmpty()) {
            EmptyContentView(
                title = "No Pending Suggestions",
                text = "All user suggestions have been reviewed.",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.spacing.outerSpacing, vertical = AppTheme.spacing.groupedVerticalElementSpacing),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (!uiState.successMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.successMessage,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.status.success,
                    )
                }
                if (!uiState.errorMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.errorMessage,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.status.error,
                    )
                }

                uiState.suggestions.forEach { item ->
                    SuggestionCard(
                        item = item,
                        isActionInProgress = uiState.isActionInProgress,
                        onApprove = { suggestion, story ->
                            onUiEvent(AdminReviewSuggestionsUiEvent.OnApproveClicked(suggestion, story))
                        },
                        onReject = { suggestionId ->
                            onUiEvent(AdminReviewSuggestionsUiEvent.OnRejectClicked(suggestionId))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionCard(
    item: SuggestionItemUiState,
    isActionInProgress: Boolean,
    onApprove: (AppStorySuggestion, AppStory) -> Unit,
    onReject: (String) -> Unit,
) {
    val suggestion = item.suggestion
    val story = item.story

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Story: ${suggestion.storyName}",
                style = AppTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
            )
            Text(
                text = "Suggested by: ${suggestion.userEmail ?: suggestion.userDisplayName ?: suggestion.userId}",
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.text.secondary,
            )

            if (!suggestion.note.isNullOrBlank()) {
                Text(
                    text = "User Note: ${suggestion.note}",
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.text.primary,
                )
            }

            // Field diffs
            if (story != null) {
                if (!suggestion.suggestedName.isNullOrBlank()) {
                    DiffSection("App Name", story.name, suggestion.suggestedName)
                }
                if (!suggestion.suggestedOneLiner.isNullOrBlank()) {
                    DiffSection("One Liner", story.oneLiner, suggestion.suggestedOneLiner)
                }
                if (!suggestion.suggestedTechStack.isNullOrBlank()) {
                    DiffSection("Tech Stack", story.techStack, suggestion.suggestedTechStack)
                }
                if (!suggestion.suggestedCategory.isNullOrBlank()) {
                    DiffSection("Category", story.category ?: "", suggestion.suggestedCategory)
                }
                if (!suggestion.suggestedPublisher.isNullOrBlank()) {
                    DiffSection("Publisher", story.publisher ?: "", suggestion.suggestedPublisher)
                }
                if (!suggestion.suggestedRevenue.isNullOrBlank()) {
                    DiffSection("Revenue", story.revenue ?: "", suggestion.suggestedRevenue)
                }
                if (!suggestion.suggestedDownloads.isNullOrBlank()) {
                    DiffSection("Downloads", story.downloads ?: "", suggestion.suggestedDownloads)
                }
                if (!suggestion.suggestedOriginStory.isNullOrBlank()) {
                    DiffSection("Origin Story", story.originStory, suggestion.suggestedOriginStory)
                }
                if (!suggestion.suggestedGrowthPlaybook.isNullOrBlank()) {
                    DiffSection("Growth Playbook", story.growthPlaybook, suggestion.suggestedGrowthPlaybook)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                if (story != null) {
                    AppButton(
                        text = "Approve",
                        onClick = { onApprove(suggestion, story) },
                        enabled = !isActionInProgress,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                AppButton(
                    text = "Reject",
                    onClick = { onReject(suggestion.id) },
                    enabled = !isActionInProgress,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun DiffSection(
    label: String,
    originalValue: String,
    suggestedValue: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary,
        )
        Text(
            text = "User Suggested: $suggestedValue",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.text.secondary,
        )
        Text(
            text = "Changes from original:",
            style = AppTheme.typography.bodyExtraSmall,
            color = AppTheme.colors.text.secondary,
        )
        val diffAnnotated = TextDiff.computeDiffAnnotatedString(originalValue, suggestedValue)
        Text(
            text = diffAnnotated,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.text.primary,
        )
    }
}
