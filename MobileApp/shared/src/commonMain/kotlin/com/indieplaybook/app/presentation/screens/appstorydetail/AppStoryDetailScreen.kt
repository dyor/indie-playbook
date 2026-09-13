package com.indieplaybook.app.presentation.screens.appstorydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.indieplaybook.app.designsystem.components.AppButton
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_back
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.domain.model.AppStory
import com.indieplaybook.app.presentation.screens.homefeed.StorePresenceBadge
import com.indieplaybook.app.presentation.screens.homefeed.StoryAvailableBadge
import com.indieplaybook.app.presentation.screens.homefeed.TechStackBadge
import com.indieplaybook.app.util.StoreDevice
import com.indieplaybook.app.util.StoreScreenshot

@Composable
fun AppStoryDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: AppStoryDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSuggestEdits: (storyId: String) -> Unit,
    onNavigateToAdminEdit: (storyId: String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppStoryDetailScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToSuggestEdits = onNavigateToSuggestEdits,
        onNavigateToAdminEdit = onNavigateToAdminEdit,
    )
}

@Composable
fun AppStoryDetailScreen(
    modifier: Modifier = Modifier,
    uiState: AppStoryDetailUiState,
    onUiEvent: (AppStoryDetailUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSuggestEdits: (storyId: String) -> Unit = {},
    onNavigateToAdminEdit: (storyId: String) -> Unit = {},
) {
    val story = uiState.story
    val localUriHandler = LocalUriHandler.current

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
                        if (!story.iconUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(story.iconUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = story.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(AppTheme.colors.outline),
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(AppTheme.colors.outline),
                                contentAlignment = Alignment.Center,
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
                                    else -> "📱"
                                }
                                Text(
                                    text = iconText,
                                    style = AppTheme.typography.h3,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = story.name,
                                style = AppTheme.typography.h5,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.text.primary,
                            )
                            if (!story.category.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = story.category,
                                    style = AppTheme.typography.bodySmall,
                                    color = AppTheme.colors.text.secondary,
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                TechStackBadge(techStack = story.techStack)

                                val storePres = story.storePresence ?: if (!story.googlePlayUrl.isNullOrBlank() && !story.appStoreUrl.isNullOrBlank()) "Dual Store" else "App Store Only"
                                StorePresenceBadge(storePresence = storePres)

                                if (story.originStory.isNotBlank()) {
                                    StoryAvailableBadge(type = "Origin", emoji = "🌱")
                                }

                                if (story.growthPlaybook.isNotBlank()) {
                                    StoryAvailableBadge(type = "Growth", emoji = "📈")
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

                // Origin Story Section (only if not blank)
                if (story.originStory.isNotBlank()) {
                    DetailSection(
                        title = "The Origin Story",
                        content = story.originStory,
                    )
                }

                // Growth Playbook Section (only if not blank)
                if (story.growthPlaybook.isNotBlank()) {
                    DetailSection(
                        title = "The Growth Playbook",
                        content = story.growthPlaybook,
                    )
                }

                // Extra App Store Details
                if (!story.appStoreAbout.isNullOrBlank() || !story.googlePlayAbout.isNullOrBlank()) {
                    DetailSection(
                        title = "Store Description",
                        content = story.appStoreAbout ?: story.googlePlayAbout ?: "",
                    )
                }

                // Links & Resources Section
                LinksSection(
                    story = story,
                    onOpenUrl = { url ->
                        try {
                            localUriHandler.openUri(url)
                        } catch (_: Exception) {
                        }
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AppButton(
                        text = "✏️ Suggest Edits",
                        onClick = { onNavigateToSuggestEdits(story.id) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (uiState.isAdmin) {
                        AppButton(
                            text = "🛠️ Direct Edit (Admin)",
                            onClick = { onNavigateToAdminEdit(story.id) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
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

@Composable
private fun LinksSection(
    story: AppStory,
    onOpenUrl: (String) -> Unit,
) {
    val hasLinks = !story.appWebsiteUrl.isNullOrBlank() ||
        !story.appStoreUrl.isNullOrBlank() ||
        !story.googlePlayUrl.isNullOrBlank() ||
        !story.appStorePrivacyPolicy.isNullOrBlank() ||
        !story.googlePlayPrivacyPolicy.isNullOrBlank()

    if (hasLinks) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Links & Resources",
                style = AppTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
            )

            if (!story.appWebsiteUrl.isNullOrBlank()) {
                LinkItemRow(
                    icon = "🌐",
                    title = "Official Website",
                    url = story.appWebsiteUrl,
                    onClick = { onOpenUrl(story.appWebsiteUrl) },
                )
            }

            if (!story.appStoreUrl.isNullOrBlank()) {
                LinkItemRow(
                    icon = "🍎",
                    title = "Apple App Store",
                    url = story.appStoreUrl,
                    onClick = { onOpenUrl(story.appStoreUrl) },
                )
            }

            if (!story.googlePlayUrl.isNullOrBlank()) {
                LinkItemRow(
                    icon = "🤖",
                    title = "Google Play Store",
                    url = story.googlePlayUrl,
                    onClick = { onOpenUrl(story.googlePlayUrl) },
                )
            }

            val privacyUrl = story.appStorePrivacyPolicy ?: story.googlePlayPrivacyPolicy
            if (!privacyUrl.isNullOrBlank()) {
                LinkItemRow(
                    icon = "🔒",
                    title = "Privacy Policy",
                    url = privacyUrl,
                    onClick = { onOpenUrl(privacyUrl) },
                )
            }
        }
    }
}

@Composable
private fun LinkItemRow(
    icon: String,
    title: String,
    url: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = icon,
                    style = AppTheme.typography.h5,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = AppTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.text.primary,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = url,
                        style = AppTheme.typography.bodySmall,
                        color = Color(0xFF64B5F6),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "↗",
                style = AppTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64B5F6),
            )
        }
    }
}

@Preview
@StoreScreenshot(device = StoreDevice.IPHONE_6_5, locale = "en", tag = "02-storydetail")
@Composable
private fun AppStoryDetailStoreScreenshot_en() {
    AppTheme {
        AppStoryDetailScreen(
            uiState = AppStoryDetailUiState(
                story = AppStory(
                    id = "1",
                    name = "Flighty",
                    oneLiner = "Live flight tracking app built with Swift and Kotlin Multiplatform",
                    category = "Travel",
                    techStack = "KMP",
                    iconUrl = "",
                    isBookmarked = true,
                    downloads = "5M+",
                    revenue = "$10M+ ARR",
                    originStory = "Started by passionate travelers wanting beautiful live tracking.",
                    growthPlaybook = "Mastered Live Activities and App Store editorial featuring.",
                    appStoreRating = 4.8,
                    appStoreReviews = 142000,
                    publisher = "Flighty App Corp",
                    releaseDate = "2019-10-01",
                ),
                isAdmin = false,
            ),
            onUiEvent = {},
            onNavigateBack = {},
            onNavigateToSuggestEdits = {},
            onNavigateToAdminEdit = {},
        )
    }
}
