package com.indieplaybook.app.presentation.screens.account

import com.indieplaybook.app.designsystem.components.SettingsItemUiState
import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_settings_item_logout
import com.indieplaybook.app.designsystem.generated.resources.ic_settings_item_subscriptions
import com.indieplaybook.app.designsystem.generated.resources.ic_settings_item_support_legal
import com.indieplaybook.app.domain.model.User
import com.indieplaybook.app.generated.resources.Res
import com.indieplaybook.app.generated.resources.help_and_support
import com.indieplaybook.app.generated.resources.logout
import com.indieplaybook.app.generated.resources.subscriptions

data class AccountUiState(
    val settingsItemList: List<SettingsItemUiState> = listOf(
        SettingsItemUiState(
            startIcon = UiRes.drawable.ic_settings_item_subscriptions,
            textRes = Res.string.subscriptions,
        ),

        SettingsItemUiState(
            startIcon = UiRes.drawable.ic_settings_item_support_legal,
            textRes = Res.string.help_and_support,
        ),

        SettingsItemUiState(
            startIcon = UiRes.drawable.ic_settings_item_logout,
            textRes = Res.string.logout,
            showEndIcon = false,
        ),
    ),
    val user: User? = null,
    val isLogoutDialogVisible: Boolean = false,
    val showUpgradePremiumBanner: Boolean = false,
)

sealed interface AccountUiEvent {
    data class OnSettingsItemClick(val item: SettingsItemUiState) : AccountUiEvent
    data object OnLogoutConfirmClick : AccountUiEvent
    data object OnLogoutDialogDismiss : AccountUiEvent
    data object OnClickUpgradePremium : AccountUiEvent
    data object OnClickSignIn : AccountUiEvent
    data object OnClickProfile : AccountUiEvent
}
