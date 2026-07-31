package com.indieplaybook.app.presentation.screens.homefeed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.domain.model.AppStory
import org.jetbrains.compose.resources.painterResource
import com.indieplaybook.app.generated.resources.Res

@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeFeedViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeFeedScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateToDetail = onNavigateToDetail
    )
}

@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    uiState: HomeFeedUiState,
    onUiEvent: (HomeFeedUiEvent) -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = false,
        title = "Discover",
        includeBottomInsets = false,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.spacing.groupedVerticalElementSpacing),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing),
                contentPadding = PaddingValues(horizontal = AppTheme.spacing.outerSpacing)
            ) {
                items(uiState.filterOptions) { filter ->
                    FilterChip(
                        text = filter,
                        isSelected = uiState.selectedFilter == filter,
                        onClick = { onUiEvent(HomeFeedUiEvent.OnFilterSelected(filter)) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = AppTheme.spacing.outerSpacing,
                    vertical = AppTheme.spacing.groupedVerticalElementSpacing
                ),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)
            ) {
                items(uiState.stories, key = { it.id }) { story ->
                    AppStoryCard(
                        story = story,
                        onClick = { onNavigateToDetail(story.id) },
                        onBookmarkClick = { onUiEvent(HomeFeedUiEvent.OnBookmarkClicked(story)) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) AppTheme.colors.primary else AppTheme.colors.surfaceContainer
    val textColor = if (isSelected) AppTheme.colors.onPrimary else AppTheme.colors.text.primary

    Surface(
        shape = CircleShape,
        color = backgroundColor,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = textColor,
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun AppStoryCard(
    story: AppStory,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AppTheme.colors.outline)
                    ) {
                         // Placeholder for icon
                         Text("A", modifier = Modifier.align(Alignment.Center), color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = story.name,
                            style = AppTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.text.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        TechStackBadge(techStack = story.techStack)
                    }
                }

                // Placeholder bookmark icon using text for now
                IconButton(onClick = onBookmarkClick, modifier = Modifier.size(32.dp)) {
                     Text(if (story.isBookmarked) "★" else "☆", color = if (story.isBookmarked) AppTheme.colors.primary else AppTheme.colors.text.secondary, style = AppTheme.typography.h5)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = story.oneLiner,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.text.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!story.downloads.isNullOrBlank() || !story.revenue.isNullOrBlank() || !story.publisher.isNullOrBlank() || !story.releaseDate.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (!story.downloads.isNullOrBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Downloads:",
                                    style = AppTheme.typography.bodySmall,
                                    color = AppTheme.colors.text.secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = story.downloads,
                                    style = AppTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colors.primary
                                )
                            }
                        }
                        if (!story.revenue.isNullOrBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Revenue:",
                                    style = AppTheme.typography.bodySmall,
                                    color = AppTheme.colors.text.secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = story.revenue,
                                    style = AppTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colors.status.success
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (!story.publisher.isNullOrBlank()) {
                            Text(
                                text = "By ${story.publisher}",
                                style = AppTheme.typography.bodySmall,
                                color = AppTheme.colors.text.secondary
                            )
                        }
                        if (!story.releaseDate.isNullOrBlank()) {
                            Text(
                                text = "Released: ${story.releaseDate}",
                                style = AppTheme.typography.bodyExtraSmall,
                                color = AppTheme.colors.text.secondary.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TechStackBadge(techStack: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(AppTheme.colors.alternative)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = techStack,
            style = AppTheme.typography.bodyExtraSmall,
            color = AppTheme.colors.onAlternative,
            fontWeight = FontWeight.Medium
        )
    }
}
