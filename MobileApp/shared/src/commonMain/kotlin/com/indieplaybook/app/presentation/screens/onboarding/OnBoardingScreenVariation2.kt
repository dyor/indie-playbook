package com.indieplaybook.app.presentation.screens.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import com.indieplaybook.app.designsystem.components.AnimatedHorizontalPager
import com.indieplaybook.app.designsystem.components.AppButton
import com.indieplaybook.app.designsystem.components.ButtonStyle
import com.indieplaybook.app.designsystem.components.Divider
import com.indieplaybook.app.designsystem.components.HorizontalPagerIndicator
import com.indieplaybook.app.designsystem.components.HorizontalPagerIndicatorStyle
import com.indieplaybook.app.designsystem.components.ScreenTitle
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.designsystem.util.defaultScreenPadding
import com.indieplaybook.app.generated.resources.Res
import com.indieplaybook.app.generated.resources.btn_get_started
import com.indieplaybook.app.generated.resources.btn_next
import com.indieplaybook.app.generated.resources.btn_skip
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue
import kotlin.math.min

@Composable
fun OnBoardingScreenVariation2(
    modifier: Modifier = Modifier,
    uiState: OnBoardingUiState,
    onUiEvent: (OnBoardingUiEvent) -> Unit,
    onNavigateToMethodology: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { uiState.pages.size },
    )
    val pagerOffset = pagerState.currentPageOffsetFraction.absoluteValue

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.primary)
                .safeDrawingPadding()
                .defaultScreenPadding(AppTheme.spacing),
        ) {
            val imageAnimationOffset = lerp(0.dp, 400.dp, pagerOffset)
            val imageAnimationAlpha = lerp(start = 0.05f, stop = 1f, fraction = 1f - pagerOffset)
            Image(
                painter = painterResource(uiState.pages[pagerState.currentPage].imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(imageAnimationAlpha)
                    .offset(y = imageAnimationOffset),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(AppTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeContent)
                .padding(bottom = AppTheme.spacing.outerSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val isLastPage = pagerState.currentPage == (pagerState.pageCount - 1)
            AnimatedHorizontalPager(
                pagerState = pagerState,
            ) { pageIndex ->
                val onBoardingScreenData = uiState.pages[pageIndex]
                OnBoardingPager(
                    item = onBoardingScreenData,
                    onNavigateToMethodology = onNavigateToMethodology,
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            top = AppTheme.spacing.largeSpacing,
                            start = AppTheme.spacing.outerSpacing,
                            end = AppTheme.spacing.outerSpacing,
                        ),
                )
            }
            HorizontalPagerIndicator(
                modifier = Modifier.padding(top = AppTheme.spacing.sectionSpacing),
                size = pagerState.pageCount,
                selectedIndex = pagerState.currentPage,
                style = HorizontalPagerIndicatorStyle.STYLE2,
                onClickIndicator = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = index,
                            animationSpec = tween(),
                        )
                    }
                },

            )
            Divider(
                modifier = Modifier.padding(top = AppTheme.spacing.largeSpacing),
            )
            Box(
                modifier = Modifier.padding(
                    top = AppTheme.spacing.sectionSpacing,
                    start = AppTheme.spacing.outerSpacing,
                    end = AppTheme.spacing.outerSpacing,
                ),
                contentAlignment = Alignment.Center,
            ) {
                if (!isLastPage) {
                    SkipAndContinueButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClickContinue = {
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
                        onClickSkip = {
                            coroutineScope.launch { pagerState.animateScrollToPage(uiState.pages.lastIndex) }
                        },
                    )
                } else {
                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.btn_get_started),
                        onClick = { onUiEvent(OnBoardingUiEvent.OnClickStart) },
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
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.groupedVerticalElementSpacing),
    ) {
        ScreenTitle(
            text = stringResource(item.title),
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(item.description),
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colors.text.secondary,
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
            Spacer(modifier = Modifier.height(4.dp))
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
                border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.5f)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
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

@Composable
private fun SkipAndContinueButton(
    modifier: Modifier = Modifier,
    onClickContinue: () -> Unit,
    onClickSkip: () -> Unit,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing),
    ) {
        AppButton(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.btn_skip),
            style = ButtonStyle.ALTERNATIVE,
            onClick = { onClickSkip() },
        )
        AppButton(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.btn_next),
            style = ButtonStyle.PRIMARY,
            onClick = { onClickContinue() },
        )
    }
}
