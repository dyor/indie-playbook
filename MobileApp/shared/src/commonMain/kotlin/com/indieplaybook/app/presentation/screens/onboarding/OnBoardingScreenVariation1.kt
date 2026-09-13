package com.indieplaybook.app.presentation.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.indieplaybook.app.designsystem.components.AnimatedHorizontalPager
import com.indieplaybook.app.designsystem.components.AppButton
import com.indieplaybook.app.designsystem.components.HorizontalPagerIndicator
import com.indieplaybook.app.designsystem.components.HorizontalPagerIndicatorStyle
import com.indieplaybook.app.designsystem.components.ScreenTitle
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.generated.resources.Res
import com.indieplaybook.app.generated.resources.btn_get_started
import com.indieplaybook.app.generated.resources.btn_next
import com.indieplaybook.app.generated.resources.btn_skip
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.min

@Composable
fun OnBoardingScreenVariation1(
    modifier: Modifier = Modifier,
    uiState: OnBoardingUiState,
    onUiEvent: (OnBoardingUiEvent) -> Unit,
    onNavigateToMethodology: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
    }

    Column(
        modifier = modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(vertical = AppTheme.spacing.largeSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { uiState.pages.size },
        )
        val isLastPage = pagerState.currentPage == (pagerState.pageCount - 1)

        Row(
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(horizontal = AppTheme.spacing.outerSpacing),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            // Skip straight to the last page; hidden once you're already there.
            AnimatedVisibility(
                visible = isLastPage.not(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                SkipButton(
                    text = stringResource(Res.string.btn_skip),
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(uiState.pages.lastIndex) }
                    },
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedHorizontalPager(
                pagerState = pagerState,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.sectionSpacing)
                    .heightIn(min = 480.dp),
            ) { pageIndex ->
                val onBoardingScreenData = uiState.pages[pageIndex]
                OnBoardingPager(
                    item = onBoardingScreenData,
                    onNavigateToMethodology = onNavigateToMethodology,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.outerSpacing),
                )
            }
        }

        HorizontalPagerIndicator(
            modifier = Modifier.padding(top = AppTheme.spacing.sectionSpacing),
            size = pagerState.pageCount,
            selectedIndex = pagerState.currentPage,
            style = HorizontalPagerIndicatorStyle.STYLE1,
            onClickIndicator = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        page = index,
                        animationSpec = tween(),
                    )
                }
            },

        )

        Column(
            modifier = Modifier.padding(
                start = AppTheme.spacing.outerSpacing,
                end = AppTheme.spacing.outerSpacing,
                top = AppTheme.spacing.sectionSpacing,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                // Last page finishes onboarding; earlier pages advance the pager.
                if (isLastPage) {
                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.btn_get_started),
                        onClick = { onUiEvent(OnBoardingUiEvent.OnClickStart) },
                    )
                } else {
                    AppButton(
                        text = stringResource(Res.string.btn_next),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            coroutineScope.launch {
                                val nextPage = min(
                                    pagerState.currentPage + 1,
                                    uiState.pages.lastIndex,
                                )
                                pagerState.animateScrollToPage(
                                    page = nextPage,
                                    animationSpec = tween(),
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun OnBoardingPager(
    modifier: Modifier = Modifier,
    item: OnBoardingScreenData,
    onNavigateToMethodology: () -> Unit = {},
) {
    val localUriHandler = LocalUriHandler.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.spacing.sectionSpacing,
            Alignment.CenterVertically,
        ),
    ) {
        Image(
            painter = painterResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier.height(200.dp),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.groupedVerticalElementSpacing),
        ) {
            ScreenTitle(
                text = stringResource(item.title),
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(item.description),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.text.primary,
                textAlign = TextAlign.Center,
            )

            if (item.hasMethodologyLink) {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onNavigateToMethodology() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colors.surfaceContainer,
                    ),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.6f)),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "📐 More about the methodology",
                            style = AppTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "➔",
                            style = AppTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary,
                        )
                    }
                }
            }

            if (!item.linkUrl.isNullOrBlank() && !item.linkText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            try {
                                localUriHandler.openUri(item.linkUrl)
                            } catch (_: Exception) {}
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colors.surfaceContainer,
                    ),
                    border = BorderStroke(1.dp, AppTheme.colors.outline),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = item.linkText,
                            style = AppTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64B5F6),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "↗",
                            style = AppTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64B5F6),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkipButton(
    text: String = stringResource(Res.string.btn_skip),
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        // This 12 dp extra padding comes from Material Design so we remove that
        modifier = modifier.offset(x = 12.dp),
        onClick = { onClick() },
    ) {
        Text(
            text = text,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.text.secondary,
        )
    }
}
