package com.indieplaybook.app.presentation.screens.onboarding

import com.indieplaybook.app.designsystem.generated.resources.UiRes
import com.indieplaybook.app.designsystem.generated.resources.ic_logo
import com.indieplaybook.app.generated.resources.Res
import com.indieplaybook.app.generated.resources.desc_onboarding_page_1
import com.indieplaybook.app.generated.resources.desc_onboarding_page_2
import com.indieplaybook.app.generated.resources.desc_onboarding_page_3
import com.indieplaybook.app.generated.resources.title_onboarding_page_1
import com.indieplaybook.app.generated.resources.title_onboarding_page_2
import com.indieplaybook.app.generated.resources.title_onboarding_page_3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class OnBoardingScreenData(
    val title: StringResource,
    val description: StringResource,
    val imageRes: DrawableResource,
    val linkText: String? = null,
    val linkUrl: String? = null,
    val hasMethodologyLink: Boolean = false,
)

data class OnBoardingUiState(
    val pages: List<OnBoardingScreenData> = listOf(
        OnBoardingScreenData(
            title = Res.string.title_onboarding_page_1,
            description = Res.string.desc_onboarding_page_1,
            imageRes = UiRes.drawable.ic_logo,
        ),
        OnBoardingScreenData(
            title = Res.string.title_onboarding_page_2,
            description = Res.string.desc_onboarding_page_2,
            imageRes = UiRes.drawable.ic_logo,
            linkText = "🌐 Visit AppFigures (appfigures.com)",
            linkUrl = "https://appfigures.com",
            hasMethodologyLink = true,
        ),
        OnBoardingScreenData(
            title = Res.string.title_onboarding_page_3,
            description = Res.string.desc_onboarding_page_3,
            imageRes = UiRes.drawable.ic_logo,
        ),
    ),
    val isOnBoardingFinished: Boolean = false,
    val isNewUser: Boolean = false,
    val isLoading: Boolean = true,
)

sealed interface OnBoardingUiEvent {
    data object OnClickStart : OnBoardingUiEvent
}
