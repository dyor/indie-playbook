package com.indieplaybook.app.presentation.screens.savedbookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.presentation.screens.homefeed.AppStoryCard

@Composable
fun SavedBookmarksScreen(
    modifier: Modifier = Modifier,
    viewModel: SavedBookmarksViewModel,
    onNavigateToDetail: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SavedBookmarksScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateToDetail = onNavigateToDetail,
    )
}

@Composable
fun SavedBookmarksScreen(
    modifier: Modifier = Modifier,
    uiState: SavedBookmarksUiState,
    onUiEvent: (SavedBookmarksUiEvent) -> Unit,
    onNavigateToDetail: (String) -> Unit,
) {
    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = false, // Set to false since we use LazyColumn
        title = "Saved Bookmarks",
        includeBottomInsets = false, // Set to false as bottom nav is visible
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (uiState.bookmarkedStories.isEmpty()) {
                Text(
                    text = "No bookmarks yet",
                    style = AppTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = AppTheme.spacing.outerSpacing,
                        vertical = AppTheme.spacing.groupedVerticalElementSpacing,
                    ),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing),
                ) {
                    items(uiState.bookmarkedStories, key = { it.id }) { story ->
                        AppStoryCard(
                            story = story,
                            onClick = { onNavigateToDetail(story.id) },
                            onBookmarkClick = { onUiEvent(SavedBookmarksUiEvent.OnBookmarkClicked(story)) },
                        )
                    }
                }
            }
        }
    }
}
