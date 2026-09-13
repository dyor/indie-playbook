package com.indieplaybook.app.presentation.screens.homefeed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_tech_flutter
import com.indieplaybook.app.designsystem.generated.resources.ic_tech_kmp
import com.indieplaybook.app.designsystem.generated.resources.ic_tech_react_native
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.domain.model.AppStory
import com.indieplaybook.app.util.StoreDevice
import com.indieplaybook.app.util.StoreScreenshot
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeFeedViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSuggestNewApp: () -> Unit = {},
    onNavigateToMethodology: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeFeedScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToSuggestNewApp = onNavigateToSuggestNewApp,
        onNavigateToMethodology = onNavigateToMethodology,
    )
}

@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    uiState: HomeFeedUiState,
    onUiEvent: (HomeFeedUiEvent) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSuggestNewApp: () -> Unit = {},
    onNavigateToMethodology: () -> Unit = {},
) {
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showAboutDialog) {
        AboutAppDialog(
            onDismiss = { showAboutDialog = false },
            onNavigateToMethodology = {
                showAboutDialog = false
                onNavigateToMethodology()
            },
        )
    }

    val appCountText = if (uiState.stories.size == 1) "1 app" else "${uiState.stories.size} apps"

    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = false,
        title = "Discover ($appCountText)",
        includeBottomInsets = false,
        toolbarExtraContent = {
            IconButton(
                onClick = { showAboutDialog = true },
                modifier = Modifier.size(36.dp),
            ) {
                Text(
                    text = "ℹ️",
                    style = AppTheme.typography.h6,
                )
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Filter Row 1: Frameworks
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = AppTheme.spacing.outerSpacing),
            ) {
                items(uiState.frameworkFilterOptions) { filter ->
                    FilterChip(
                        text = filter,
                        isSelected = uiState.selectedFrameworkFilter == filter,
                        onClick = { onUiEvent(HomeFeedUiEvent.OnFrameworkFilterSelected(filter)) },
                    )
                }
            }

            // Filter Row 2: Store Presence
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = AppTheme.spacing.outerSpacing),
            ) {
                items(uiState.storeFilterOptions) { filter ->
                    FilterChip(
                        text = filter,
                        isSelected = uiState.selectedStoreFilter == filter,
                        onClick = { onUiEvent(HomeFeedUiEvent.OnStoreFilterSelected(filter)) },
                    )
                }
            }

            // Filter Row 3: Story Content
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = AppTheme.spacing.outerSpacing),
            ) {
                items(uiState.storyFilterOptions) { filter ->
                    FilterChip(
                        text = filter,
                        isSelected = uiState.selectedStoryFilter == filter,
                        onClick = { onUiEvent(HomeFeedUiEvent.OnStoryFilterSelected(filter)) },
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = AppTheme.spacing.outerSpacing,
                    vertical = AppTheme.spacing.groupedVerticalElementSpacing,
                ),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing),
            ) {
                items(uiState.stories, key = { it.id }) { story ->
                    AppStoryCard(
                        story = story,
                        onClick = { onNavigateToDetail(story.id) },
                        onBookmarkClick = { onUiEvent(HomeFeedUiEvent.OnBookmarkClicked(story)) },
                    )
                }

                item {
                    SuggestNewAppCard(
                        onClick = onNavigateToSuggestNewApp,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit,
    onNavigateToMethodology: () -> Unit = {},
) {
    val localUriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = AppTheme.colors.primary)
            }
        },
        title = {
            Text(
                text = "About Indie Playbook 📖",
                style = AppTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.text.primary,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Indie Playbook is a curated directory of the top independent mobile apps (based on downloads from the App Store) built with Cross-Platform frameworks: React Native, Flutter, and Kotlin Multiplatform (KMP).",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.text.primary,
                )

                Text(
                    text = "Learn real tech stacks, architectural choices, origin stories, and growth playbooks behind successful indie apps.",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.text.secondary,
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onNavigateToMethodology() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.6f)),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "📐 Detailed Dataset Methodology",
                                style = AppTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.primary,
                            )
                            Text(
                                text = "See the full filtering funnel and metrics breakdown.",
                                style = AppTheme.typography.bodyExtraSmall,
                                color = AppTheme.colors.text.secondary,
                            )
                        }
                        Text(
                            text = "➔",
                            style = AppTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary,
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            try {
                                localUriHandler.openUri("https://appfigures.com")
                            } catch (_: Exception) {}
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
                    border = BorderStroke(1.dp, AppTheme.colors.outline),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = "📊 Ranking Data Provided by AppFigures",
                            style = AppTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.text.primary,
                        )
                        Text(
                            text = "Initial app download estimates, rankings, and store metrics were provided by AppFigures. A huge thank you to AppFigures for providing such a valuable dataset to the independent cross platform developer community!",
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colors.text.secondary,
                        )
                        Text(
                            text = "Visit appfigures.com ↗",
                            style = AppTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64B5F6),
                        )
                    }
                }
            }
        },
        containerColor = AppTheme.colors.background,
    )
}

@Composable
fun SuggestNewAppCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceContainer,
        ),
        border = BorderStroke(1.5.dp, AppTheme.colors.primary.copy(alpha = 0.4f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppTheme.colors.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "💡",
                    style = AppTheme.typography.h5,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Suggest a New App",
                    style = AppTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.text.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Know an indie cross-platform app that should be featured? Tap to submit!",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.text.secondary,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "➔",
                style = AppTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val platformColor = when (text) {
        "Flutter" -> Color(0xFF02569B)
        "React Native" -> Color(0xFF20232A)
        "KMP" -> Color(0xFF7F52FF)
        "Dual Store" -> Color(0xFF028090)
        "App Store Only" -> Color(0xFF6A4C93)
        "Origin Story" -> Color(0xFF2D6A4F)
        "Growth Story" -> Color(0xFFD68C45)
        else -> AppTheme.colors.primary
    }

    val backgroundColor = if (isSelected) platformColor else AppTheme.colors.surfaceContainer
    val textColor = if (isSelected) Color.White else AppTheme.colors.text.primary

    val iconRes = when (text) {
        "Flutter" -> UiRes.drawable.ic_tech_flutter
        "React Native" -> UiRes.drawable.ic_tech_react_native
        "KMP" -> UiRes.drawable.ic_tech_kmp
        else -> null
    }

    val emojiPrefix = when (text) {
        "Dual Store" -> "📲 "
        "App Store Only" -> "🍎 "
        "Origin Story" -> "🌱 "
        "Growth Story" -> "📈 "
        else -> ""
    }

    Surface(
        shape = CircleShape,
        color = backgroundColor,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = text,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(15.dp),
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = "$emojiPrefix$text",
                color = textColor,
                style = AppTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppStoryCard(
    story: AppStory,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
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
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AppTheme.colors.outline),
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
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
                            Text(iconText, style = AppTheme.typography.h5)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = story.name,
                            style = AppTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.text.primary,
                        )
                        if (!story.category.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = story.category,
                                style = AppTheme.typography.bodyExtraSmall,
                                color = AppTheme.colors.text.secondary,
                            )
                        }
                    }
                }

                // Bookmark icon
                IconButton(onClick = onBookmarkClick, modifier = Modifier.size(32.dp)) {
                    Text(
                        if (story.isBookmarked) "★" else "☆",
                        color = if (story.isBookmarked) AppTheme.colors.primary else AppTheme.colors.text.secondary,
                        style = AppTheme.typography.h5,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: TechStack, StorePresence, Origin, Growth
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = story.oneLiner,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.text.secondary,
            )

            if (!story.downloads.isNullOrBlank() || !story.revenue.isNullOrBlank() || !story.publisher.isNullOrBlank() || !story.releaseDate.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
                    }

                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (!story.publisher.isNullOrBlank()) {
                            Text(
                                text = "By ${story.publisher}",
                                style = AppTheme.typography.bodySmall,
                                color = AppTheme.colors.text.secondary,
                            )
                        }
                        if (!story.releaseDate.isNullOrBlank()) {
                            Text(
                                text = "Released: ${story.releaseDate}",
                                style = AppTheme.typography.bodyExtraSmall,
                                color = AppTheme.colors.text.secondary.copy(alpha = 0.7f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StorePresenceBadge(storePresence: String) {
    val isDual = storePresence == "Dual Store"
    val backgroundColor = if (isDual) Color(0xFF0F3A4A) else Color(0xFF332042)
    val textColor = if (isDual) Color(0xFF48CAE4) else Color(0xFFC77DFF)
    val emoji = if (isDual) "📲" else "🍎"
    val label = if (isDual) "Dual Store" else "App Store Only"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 7.dp, vertical = 3.dp),
    ) {
        Text(
            text = "$emoji $label",
            style = AppTheme.typography.bodyExtraSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun StoryAvailableBadge(type: String, emoji: String) {
    val backgroundColor = if (type == "Origin") Color(0xFF1B3B2B) else Color(0xFF3B2B1B)
    val textColor = if (type == "Origin") Color(0xFF52B788) else Color(0xFFFFB703)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 7.dp, vertical = 3.dp),
    ) {
        Text(
            text = "$emoji $type",
            style = AppTheme.typography.bodyExtraSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun TechStackBadge(techStack: String) {
    val backgroundColor = when (techStack) {
        "Flutter" -> Color(0xFF02569B)
        "React Native" -> Color(0xFF20232A)
        "KMP" -> Color(0xFF7F52FF)
        else -> AppTheme.colors.alternative
    }

    val iconRes = when (techStack) {
        "Flutter" -> UiRes.drawable.ic_tech_flutter
        "React Native" -> UiRes.drawable.ic_tech_react_native
        "KMP" -> UiRes.drawable.ic_tech_kmp
        else -> null
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 7.dp, vertical = 3.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = techStack,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(12.dp),
                )
            }
            Text(
                text = techStack,
                style = AppTheme.typography.bodyExtraSmall,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview
@StoreScreenshot(device = StoreDevice.IPHONE_6_5, locale = "en", tag = "01-homefeed")
@Composable
private fun HomeFeedStoreScreenshot_en() {
    AppTheme {
        HomeFeedScreen(
            uiState = HomeFeedUiState(
                stories = listOf(
                    AppStory(
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
                        storePresence = "Dual Store",
                    ),
                    AppStory(
                        id = "2",
                        name = "Capacities",
                        oneLiner = "A studio for your mind — networked note-taking app",
                        category = "Productivity",
                        techStack = "Flutter",
                        iconUrl = "",
                        isBookmarked = false,
                        downloads = "1M+",
                        revenue = "$2M+ ARR",
                        originStory = "Built to rethink personal knowledge management.",
                        growthPlaybook = "Built in public with strong community advocacy.",
                        storePresence = "Dual Store",
                    ),
                ),
            ),
            onUiEvent = {},
            onNavigateToDetail = {},
            onNavigateToSuggestNewApp = {},
            onNavigateToMethodology = {},
        )
    }
}
