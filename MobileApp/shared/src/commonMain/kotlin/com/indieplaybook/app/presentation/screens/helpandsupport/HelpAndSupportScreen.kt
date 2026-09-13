package com.indieplaybook.app.presentation.screens.helpandsupport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.indieplaybook.app.designsystem.components.ScreenWithToolbar
import com.indieplaybook.app.designsystem.components.SettingItemListContainer
import com.indieplaybook.app.designsystem.components.SettingsItemUiState
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_back
import com.indieplaybook.app.designsystem.generated.resources.privacy_policy
import com.indieplaybook.app.designsystem.generated.resources.terms_conditions
import com.indieplaybook.app.designsystem.theme.AppTheme
import com.indieplaybook.app.generated.resources.Res
import com.indieplaybook.app.generated.resources.help_and_support
import com.indieplaybook.app.generated.resources.item_about_app
import com.indieplaybook.app.generated.resources.item_contact_support
import com.indieplaybook.app.generated.resources.item_methodology
import com.indieplaybook.app.presentation.screens.homefeed.AboutAppDialog
import com.indieplaybook.app.root.AppConfiguration
import com.indieplaybook.app.util.AppUtil
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun HelpAndSupportScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
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

    val itemList = listOf(
        SettingsItemUiState(textRes = Res.string.item_about_app),
        SettingsItemUiState(textRes = Res.string.item_methodology),
        SettingsItemUiState(textRes = Res.string.item_contact_support),
        SettingsItemUiState(textRes = UiRes.string.privacy_policy),
        SettingsItemUiState(textRes = UiRes.string.terms_conditions),
    )
    val localUriHandler = LocalUriHandler.current
    val appUtil = koinInject<AppUtil>()
    ScreenWithToolbar(
        modifier = modifier.fillMaxSize().background(AppTheme.colors.background),
        title = stringResource(Res.string.help_and_support),
        includeBottomInsets = true,
        isScrollableContent = true,
        onNavigationIconClick = onNavigateBack,
        navigationIcon = UiRes.drawable.ic_back,
    ) {
        SettingItemListContainer(
            itemList = itemList,
            itemTextStyle = AppTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold),
            onClick = {
                when (it.textRes) {
                    Res.string.item_about_app -> {
                        showAboutDialog = true
                    }

                    Res.string.item_methodology -> {
                        onNavigateToMethodology()
                    }

                    Res.string.item_contact_support -> {
                        appUtil.openFeedbackMail()
                    }

                    UiRes.string.privacy_policy -> {
                        localUriHandler.openUri(AppConfiguration.URL_PRIVACY_POLICY)
                    }

                    UiRes.string.terms_conditions -> {
                        localUriHandler.openUri(AppConfiguration.URL_TERMS_CONDITIONS)
                    }
                }
            },
        )
    }
}

@Preview
@Composable
private fun HelpAndSupportScreenPreview() {
    AppTheme {
        HelpAndSupportScreen(onNavigateBack = {})
    }
}
