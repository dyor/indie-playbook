package com.indieplaybook.app.example

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.domain.model.AppStory
import com.indieplaybook.app.presentation.screens.homefeed.HomeFeedScreen
import com.indieplaybook.app.presentation.screens.homefeed.HomeFeedUiEvent
import com.indieplaybook.app.presentation.screens.homefeed.HomeFeedUiState
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class SampleComposeUiTest {

    @Test
    fun `home feed renders app story items and filter chips`() = runComposeUiTest {
        setContent {
            AppTheme {
                HomeFeedScreen(
                    uiState = HomeFeedUiState(
                        stories = listOf(
                            AppStory(
                                id = "test-1",
                                name = "TestApp",
                                oneLiner = "An amazing cross-platform app",
                                category = "Productivity",
                                techStack = "KMP",
                                iconUrl = "",
                                isBookmarked = false,
                                originStory = "Created to solve testing",
                                growthPlaybook = "Word of mouth",
                            ),
                        ),
                    ),
                    onUiEvent = {},
                    onNavigateToDetail = {},
                    onNavigateToSuggestNewApp = {},
                )
            }
        }

        onNodeWithText("TestApp").assertIsEnabled()
        onNodeWithText("An amazing cross-platform app").assertIsEnabled()
        onNodeWithText("Suggest a New App").assertIsEnabled()
    }

    @Test
    fun `clicking filter chip emits OnFrameworkFilterSelected`() = runComposeUiTest {
        val events = mutableListOf<HomeFeedUiEvent>()

        setContent {
            AppTheme {
                HomeFeedScreen(
                    uiState = HomeFeedUiState(
                        frameworkFilterOptions = listOf("All", "Flutter", "React Native", "KMP"),
                    ),
                    onUiEvent = { events += it },
                    onNavigateToDetail = {},
                    onNavigateToSuggestNewApp = {},
                )
            }
        }

        onNodeWithText("Flutter")
            .assertIsEnabled()
            .performClick()

        assertTrue(
            events.any { it is HomeFeedUiEvent.OnFrameworkFilterSelected && it.filter == "Flutter" },
            "expected OnFrameworkFilterSelected(Flutter), got $events",
        )
    }
}
