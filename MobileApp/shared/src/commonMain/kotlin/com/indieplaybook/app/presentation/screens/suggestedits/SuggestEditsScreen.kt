package com.indieplaybook.app.presentation.screens.suggestedits

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
import androidx.compose.ui.tooling.preview.Preview
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
import com.indieplaybook.app.util.StoreDevice
import com.indieplaybook.app.util.StoreScreenshot

@Composable
fun SuggestEditsScreen(
    modifier: Modifier = Modifier,
    viewModel: SuggestEditsViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SuggestEditsScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun SuggestEditsScreen(
    modifier: Modifier = Modifier,
    uiState: SuggestEditsUiState,
    onUiEvent: (SuggestEditsUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val isNewApp = uiState.storyId == "new" || uiState.originalStory == null
    val screenTitle = if (isNewApp) "Suggest a New App" else "Suggest Edits"

    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = screenTitle,
        onNavigationIconClick = onNavigateBack,
        navigationIcon = UiRes.drawable.ic_back,
        includeBottomInsets = true,
    ) {
        if (uiState.isLoading) {
            LoadingProgress(mode = LoadingProgressMode.FULLSCREEN)
        } else if (uiState.isSubmittedSuccessfully) {
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
                            text = if (isNewApp) "App Nomination Submitted! 🎉" else "Suggestion Submitted! 🎉",
                            style = AppTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary,
                        )
                        Text(
                            text = if (isNewApp) {
                                "Thank you for contributing to the community! An admin will review the suggested app and feature it in the playbook shortly."
                            } else {
                                "Thank you for contributing. An admin will review your suggested edits shortly."
                            },
                            style = AppTheme.typography.bodyLarge,
                            color = AppTheme.colors.text.primary,
                        )
                    }
                }
                AppButton(
                    text = if (isNewApp) "Back to Discover" else "Back to Story",
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
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.status.error,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }

                UserInput(
                    label = "App Name (e.g. Flighty)",
                    value = uiState.name,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnNameChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "One Liner Description",
                    value = uiState.oneLiner,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnOneLinerChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Tech Stack / Framework (React Native, Flutter, KMP)",
                    value = uiState.techStack,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnTechStackChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Category (e.g. Travel, Productivity, Health)",
                    value = uiState.category,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnCategoryChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Publisher / Maker",
                    value = uiState.publisher,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnPublisherChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Revenue (e.g. $100K/mo, $2M ARR)",
                    value = uiState.revenue,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnRevenueChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "Downloads (e.g. 500K+, 1M+)",
                    value = uiState.downloads,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnDownloadsChanged(it)) },
                    singleLine = true,
                )

                UserInput(
                    label = "The origin story defines what motivated the developer to create this app - were they trying to solve their own problem or did they see an underserved niche? Include details on how the first version was built.",
                    value = uiState.originStory,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnOriginStoryChanged(it)) },
                    singleLine = false,
                    minLines = 4,
                )

                UserInput(
                    label = "The growth story tells what strategy this app leverages to gain users and grow revenue. Are they active on socials (TikTok, X, Instagram)? Do they use paid user acquisition, App Store Optimization (ASO), or viral referral loops?",
                    value = uiState.growthPlaybook,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnGrowthPlaybookChanged(it)) },
                    singleLine = false,
                    minLines = 4,
                )

                UserInput(
                    label = "Note to Reviewer (Optional)",
                    value = uiState.note,
                    onValueChange = { onUiEvent(SuggestEditsUiEvent.OnNoteChanged(it)) },
                    singleLine = false,
                    minLines = 2,
                )

                if (!uiState.errorMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.errorMessage,
                        style = AppTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.status.error,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                AppButton(
                    text = if (uiState.isSubmitting) {
                        "Submitting..."
                    } else if (isNewApp) {
                        "Submit New App"
                    } else {
                        "Submit Suggestion"
                    },
                    onClick = { onUiEvent(SuggestEditsUiEvent.OnSubmitClicked) },
                    enabled = !uiState.isSubmitting,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@StoreScreenshot(device = StoreDevice.IPHONE_6_5, locale = "en", tag = "04-suggestedits")
@Composable
private fun SuggestEditsStoreScreenshot_en() {
    AppTheme {
        SuggestEditsScreen(
            uiState = SuggestEditsUiState(
                storyId = "new",
                name = "MyIndieApp",
                oneLiner = "Smart habit tracking built with Flutter",
                techStack = "Flutter",
                category = "Productivity",
                downloads = "50K+",
                isLoading = false,
            ),
            onUiEvent = {},
            onNavigateBack = {},
        )
    }
}
