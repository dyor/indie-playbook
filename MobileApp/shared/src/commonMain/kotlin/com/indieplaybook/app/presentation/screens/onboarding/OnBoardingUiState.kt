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
)

data class OnBoardingUiState(

    val pages: List<OnBoardingScreenData> = listOf(
        OnBoardingScreenData(
            Res.string.title_onboarding_page_1,
            Res.string.desc_onboarding_page_1,
            UiRes.drawable.ic_logo,
        ),
        OnBoardingScreenData(
            Res.string.title_onboarding_page_2,
            Res.string.desc_onboarding_page_2,
            UiRes.drawable.ic_logo,
        ),
        OnBoardingScreenData(
            Res.string.title_onboarding_page_3,
            Res.string.desc_onboarding_page_3,
            UiRes.drawable.ic_logo,
        ),
    ),
    val isOnBoardingFinished: Boolean = false,
    // isNewUser distinguishes a fresh completion from an already-onboarded user
    val isNewUser: Boolean = false,
    val isLoading: Boolean = true,
)

sealed interface OnBoardingUiEvent {
    data object OnClickStart : OnBoardingUiEvent
}
