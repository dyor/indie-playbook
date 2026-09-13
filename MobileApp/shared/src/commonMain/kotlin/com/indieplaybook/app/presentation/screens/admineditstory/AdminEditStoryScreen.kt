package com.indieplaybook.app.presentation.screens.admineditstory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indieplaybook.app.designsystem.components.AppButton
import com.indieplaybook.app.designsystem.components.AppCardContainer
import com.indieplaybook.app.designsystem.components.LoadingProgress
import com.indieplaybook.app.designsystem.components.LoadingProgressMode
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.components.UserInput
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_back
import com.indieplaybook.app.designsystem.theme.AppTheme

@Composable
fun AdminEditStoryScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminEditStoryViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminEditStoryScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun AdminEditStoryScreen(
    modifier: Modifier = Modifier,
    uiState: AdminEditStoryUiState,
    onUiEvent: (AdminEditStoryUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = "Direct Edit (Admin)",
        onNavigationIconClick = onNavigateBack,
        navigationIcon = UiRes.drawable.ic_back,
        includeBottomInsets = true,
    ) {
        if (uiState.isLoading) {
            LoadingProgress(mode = LoadingProgressMode.FULLSCREEN)
        } else if (uiState.isSavedSuccessfully) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppTheme.spacing.outerSpacing),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing),
            ) {
                AppCardContainer {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = "Story Updated! ✅",
                            style = AppTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary,
                        )
                        Text(
                            text = "The story was updated directly in Firestore and synced locally.",
                            style = AppTheme.typography.bodyLarge,
                            color = AppTheme.colors.text.primary,
                        )
                    }
                }
                AppButton(
                    text = "Back to Details",
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.spacing.outerSpacing, vertical = AppTheme.spacing.groupedVerticalElementSpacing),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (!uiState.errorMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.errorMessage,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.status.error,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

                UserInput(
                    label = "App Name",
                    value = uiState.name,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnNameChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "One Liner",
                    value = uiState.oneLiner,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnOneLinerChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Tech Stack / Framework",
                    value = uiState.techStack,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnTechStackChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Category",
                    value = uiState.category,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnCategoryChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Publisher",
                    value = uiState.publisher,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnPublisherChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Revenue",
                    value = uiState.revenue,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnRevenueChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Downloads",
                    value = uiState.downloads,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnDownloadsChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Origin Story",
                    value = uiState.originStory,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnOriginStoryChanged(it)) },
                    singleLine = false,
                    minLines = 4,
                )

                UserInput(
                    label = "Growth Playbook",
                    value = uiState.growthPlaybook,
                    onValueChange = { onUiEvent(AdminEditStoryUiEvent.OnGrowthPlaybookChanged(it)) },
                    singleLine = false,
                    minLines = 4,
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppButton(
                    text = if (uiState.isSaving) "Saving..." else "Save Direct Edit",
                    onClick = { onUiEvent(AdminEditStoryUiEvent.OnSaveClicked) },
                    enabled = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
