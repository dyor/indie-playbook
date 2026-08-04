package com.indieplaybook.app.presentation.screens.appstorydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_back
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.presentation.screens.homefeed.TechStackBadge

@Composable
fun AppStoryDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: AppStoryDetailViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppStoryDetailScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun AppStoryDetailScreen(
    modifier: Modifier = Modifier,
    uiState: AppStoryDetailUiState,
    onUiEvent: (AppStoryDetailUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val story = uiState.story

    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = story?.name ?: "App Story",
        onNavigationIconClick = onNavigateBack,
        navigationIcon = UiRes.drawable.ic_back,
        includeBottomInsets = true,
        toolbarExtraContent = {
            if (story != null) {
                IconButton(
                    onClick = { onUiEvent(AppStoryDetailUiEvent.OnBookmarkClicked) },
                    modifier = Modifier.size(32.dp),
                ) {
                    Text(
                        text = if (story.isBookmarked) "★" else "☆",
                        color = if (story.isBookmarked) AppTheme.colors.primary else AppTheme.colors.text.secondary,
                        style = AppTheme.typography.h5,
                    )
                }
            }
        },
    ) {
        if (story != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.spacing.outerSpacing, vertical = AppTheme.spacing.groupedVerticalElementSpacing),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing),
            ) {
                // Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(AppTheme.colors.outline),
                        ) {
                            val iconText = when (story.category) {
                                "Photo & Video" -> "📸"
                                "Lifestyle" -> "🛋️"
                                "Health & Fitness" -> "💪"
                                "Sports" -> "⚽️"
                                "Education" -> "📚"
                                "Finance" -> "💰"
                                "Productivity" -> "🚀"
                                "Shopping" -> "🛍️"
                                "Entertainment" -> "🍿"
                                "Music" -> "🎵"
                                "Travel" -> "✈️"
                                "Navigation" -> "🗺️"
                                "Reference" -> "📖"
                                "Social Networking" -> "💬"
                                "Business" -> "🏢"
                                "Utilities" -> "🛠️"
                                "Food & Drink" -> "🍔"
                                else -> "✨"
                            }
                            Text(
                                text = iconText,
                                modifier = Modifier.align(Alignment.Center),
                                style = AppTheme.typography.h3,
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = story.name,
                                style = AppTheme.typography.h5,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.text.primary,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TechStackBadge(techStack = story.techStack)
                                if (!story.category.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = story.category,
                                        style = AppTheme.typography.bodyExtraSmall,
                                        color = AppTheme.colors.text.secondary,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (story.appStoreRating != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Rating:",
                                        style = AppTheme.typography.bodySmall,
                                        color = AppTheme.colors.text.secondary,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${story.appStoreRating} ★ (${story.appStoreReviews})",
                                        style = AppTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.primary,
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            if (!story.downloads.isNullOrBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Downloads:",
                                        style = AppTheme.typography.bodySmall,
                                        color = AppTheme.colors.text.secondary,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = story.downloads,
                                        style = AppTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.primary,
                                    )
                                }
                            }
                            if (!story.revenue.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Revenue:",
                                        style = AppTheme.typography.bodySmall,
                                        color = AppTheme.colors.text.secondary,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = story.revenue,
                                        style = AppTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.status.success,
                                    )
                                }
                            }
                            if (!story.publisher.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Publisher: ${story.publisher}",
                                    style = AppTheme.typography.bodySmall,
                                    color = AppTheme.colors.text.secondary,
                                )
                            }
                            if (!story.releaseDate.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Released: ${story.releaseDate}",
                                    style = AppTheme.typography.bodyExtraSmall,
                                    color = AppTheme.colors.text.secondary.copy(alpha = 0.7f),
                                )
                            }
                        }
                    }
                }

                // Origin Story Section
                DetailSection(
                    title = "The Origin Story",
                    content = story.originStory,
                )

                // Growth Playbook Section
                DetailSection(
                    title = "The Growth Playbook",
                    content = story.growthPlaybook,
                )

                // Extra App Store Details
                if (!story.appStoreAbout.isNullOrBlank() || !story.googlePlayAbout.isNullOrBlank()) {
                    DetailSection(
                        title = "Store Description",
                        content = story.appStoreAbout ?: story.googlePlayAbout ?: "",
                    )
                }

                if (!story.googlePlayUrl.isNullOrBlank()) {
                    Text(
                        text = "Play Store: ${story.googlePlayUrl}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.primary,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }

                if (!story.appStoreUrl.isNullOrBlank()) {
                    Text(
                        text = "App Store: ${story.appStoreUrl}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.primary,
                    )
                }

                if (!story.appWebsiteUrl.isNullOrBlank()) {
                    Text(
                        text = "Website: ${story.appWebsiteUrl}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.groupedVerticalElementSpacing),
    ) {
        Text(
            text = title,
            style = AppTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary,
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
        ) {
            Text(
                text = content,
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.text.primary,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
