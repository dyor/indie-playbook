package com.indieplaybook.app.presentation.screens.methodology

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_back
import com.indieplaybook.app.designsystem.generated.resources.ic_tech_flutter
import com.indieplaybook.app.designsystem.generated.resources.ic_tech_kmp
import com.indieplaybook.app.designsystem.generated.resources.ic_tech_react_native
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.util.StoreDevice
import com.indieplaybook.app.util.StoreScreenshot
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MethodologyScreen(
    modifier: Modifier = Modifier,
    viewModel: MethodologyViewModel,
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MethodologyScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun MethodologyScreen(
    modifier: Modifier = Modifier,
    uiState: MethodologyUiState,
    onUiEvent: (MethodologyUiEvent) -> Unit,
    onNavigateBack: () -> Unit = {},
) {
    val localUriHandler = LocalUriHandler.current

    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = "Dataset Methodology",
        onNavigationIconClick = onNavigateBack,
        navigationIcon = UiRes.drawable.ic_back,
        includeBottomInsets = true,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.spacing.outerSpacing, vertical = AppTheme.spacing.groupedVerticalElementSpacing),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Intro overview card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "🌳 Dataset Breakdown & Hierarchical Metrics",
                        style = AppTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.primary,
                    )
                    Text(
                        text = "We tracked App Store rankings and monthly download estimates from October 2025 to June 2026 across top 10,000 monthly ranking slots (~17,000 unique apps observed) to identify top indie cross-platform apps and their store distribution.",
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.text.secondary,
                    )
                }
            }

            // Funnel items
            MethodologyMetricCard(
                icon = "📦",
                title = "Entire Report Dataset (Released ≥ June 2025)",
                badgeText = "3,169 apps",
                badgeColor = Color(0xFF00B4D8),
                subtitle = "Filtered to apps published on or after June 1, 2025.",
            )

            MethodologyMetricCard(
                icon = "🌐",
                title = "English Language Apps",
                badgeText = "2,374 apps",
                badgeColor = Color(0xFF0096C7),
                subtitle = "out of 3,169 total apps (74.9%)",
                indent = 1,
            )

            MethodologyMetricCard(
                icon = "💡",
                title = "Active Independent Apps in English",
                badgeText = "327 apps",
                badgeColor = Color(0xFF48CAE4),
                subtitle = "out of 368 total · 41 apps or 11.1% have been removed · 119 apps or 32.3% are dual store\n\n" +
                    "Definition: Excludes government apps and corporate legacy brands. Focused purely on independent creators and bootstrapped software studios.",
                indent = 2,
            )

            MethodologyMetricCard(
                icon = "🌐",
                title = "Cross-Platform Apps (React Native, Flutter, KMP)",
                badgeText = "71 apps",
                badgeColor = Color(0xFF7209B7),
                subtitle = "19.3% of independent · 39 or 54.9% dual store · 32 or 45.1% app store only",
                indent = 3,
            )

            // React Native
            FrameworkMetricCard(
                iconRes = UiRes.drawable.ic_tech_react_native,
                title = "English Independent React Native",
                badgeText = "35 apps",
                badgeColor = Color(0xFF61DAFB),
                statsText = "21 or 60.0% dual store · 14 or 40.0% app store only",
                indent = 4,
            )

            // Flutter
            FrameworkMetricCard(
                iconRes = UiRes.drawable.ic_tech_flutter,
                title = "English Independent Flutter",
                badgeText = "30 apps",
                badgeColor = Color(0xFF47C5FB),
                statsText = "15 or 50.0% dual store · 15 or 50.0% app store only",
                indent = 4,
            )

            // KMP
            FrameworkMetricCard(
                iconRes = UiRes.drawable.ic_tech_kmp,
                title = "English Independent Kotlin Multiplatform (KMP)",
                badgeText = "6 apps",
                badgeColor = Color(0xFFFF8C00),
                statsText = "3 or 50.0% dual store · 3 or 50.0% app store only",
                indent = 4,
            )

            // Native Apps
            MethodologyMetricCard(
                icon = "📱",
                title = "Native Apps (iOS Native)",
                badgeText = "297 apps",
                badgeColor = Color(0xFF3A86FF),
                subtitle = "80.7% of independent · 80 or 26.9% dual store · 217 or 73.1% app store only",
                indent = 3,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // AppFigures attribution card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable {
                        try {
                            localUriHandler.openUri("https://appfigures.com")
                        } catch (_: Exception) {}
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
                border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.5f)),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "📊 App Store Data Source: AppFigures",
                        style = AppTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.text.primary,
                    )
                    Text(
                        text = "Initial app download estimates to identify top 10K apps were compiled using AppFigures data. We are deeply grateful to AppFigures for sharing this information with the indie development community.",
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.text.secondary,
                    )
                    Text(
                        text = "Visit AppFigures (appfigures.com) ↗",
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
private fun MethodologyMetricCard(
    icon: String,
    title: String,
    badgeText: String,
    badgeColor: Color,
    subtitle: String,
    indent: Int = 0,
) {
    val indentPadding = (indent * 10).dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indentPadding),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = icon, style = AppTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = AppTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.text.primary,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                MetricPillBadge(text = badgeText, backgroundColor = badgeColor)
            }

            Text(
                text = subtitle,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.text.secondary,
            )
        }
    }
}

@Composable
private fun FrameworkMetricCard(
    iconRes: DrawableResource,
    title: String,
    badgeText: String,
    badgeColor: Color,
    statsText: String,
    indent: Int = 0,
) {
    val indentPadding = (indent * 10).dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indentPadding),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = title,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = AppTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.text.primary,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                MetricPillBadge(text = badgeText, backgroundColor = badgeColor)
            }

            Text(
                text = "($statsText)",
                style = AppTheme.typography.bodyExtraSmall,
                color = AppTheme.colors.text.secondary,
                modifier = Modifier.padding(start = 26.dp),
            )
        }
    }
}

@Composable
private fun MetricPillBadge(
    text: String,
    backgroundColor: Color,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor.copy(alpha = 0.2f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = AppTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = backgroundColor,
        )
    }
}

@Preview
@StoreScreenshot(device = StoreDevice.IPHONE_6_5, locale = "en", tag = "03-methodology")
@Composable
private fun MethodologyStoreScreenshot_en() {
    AppTheme {
        MethodologyScreen(
            uiState = MethodologyUiState(),
            onUiEvent = {},
            onNavigateBack = {},
        )
    }
}
