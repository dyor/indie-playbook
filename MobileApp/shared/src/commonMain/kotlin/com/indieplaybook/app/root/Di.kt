package com.indieplaybook.app.root

import com.indieplaybook.app.auth.api.AuthServiceProvider
import com.indieplaybook.app.auth.api.AuthServiceProviderFactory
import com.indieplaybook.app.common.BuildConfig
import com.indieplaybook.app.data.BackgroundExecutor
import com.indieplaybook.app.data.repository.AppStoryRepository
import com.indieplaybook.app.data.repository.CreditRepository
import com.indieplaybook.app.data.repository.GenerationRepository
import com.indieplaybook.app.data.repository.SubscriptionRepository
import com.indieplaybook.app.data.repository.UserRepository
import com.indieplaybook.app.data.source.ai.OpenAiImageGenerationProvider
import com.indieplaybook.app.data.source.ai.ReplicateGenerationProvider
import com.indieplaybook.app.data.source.preferences.PreferencesDataStoreProvider
import com.indieplaybook.app.data.source.preferences.UserPreferences
import com.indieplaybook.app.data.source.preferences.UserPreferencesImpl
import com.indieplaybook.app.data.source.remote.HttpClientFactory
import com.indieplaybook.app.data.source.remote.apiservices.ApiService
import com.indieplaybook.app.data.source.remote.apiservices.TemporaryFileUploadApiService
import com.indieplaybook.app.data.source.remote.apiservices.ai.AiTransport
import com.indieplaybook.app.data.source.remote.apiservices.ai.OpenAiApiService
import com.indieplaybook.app.data.source.remote.apiservices.ai.ReplicateApiService
import com.indieplaybook.app.domain.model.credit.creditSystemConfig
import com.indieplaybook.app.domain.usecase.AiGenerationProvider
import com.indieplaybook.app.presentation.screens.account.AccountViewModel
import com.indieplaybook.app.presentation.screens.admineditstory.AdminEditStoryViewModel
import com.indieplaybook.app.presentation.screens.adminreviewsuggestions.AdminReviewSuggestionsViewModel
import com.indieplaybook.app.presentation.screens.appstorydetail.AppStoryDetailViewModel
import com.indieplaybook.app.presentation.screens.creditbalance.CreditBalanceViewModel
import com.indieplaybook.app.presentation.screens.gallery.GalleryViewModel
import com.indieplaybook.app.presentation.screens.generationresult.GenerationResultViewModel
import com.indieplaybook.app.presentation.screens.home.HomeViewModel
import com.indieplaybook.app.presentation.screens.homefeed.HomeFeedViewModel
import com.indieplaybook.app.presentation.screens.methodology.MethodologyViewModel
import com.indieplaybook.app.presentation.screens.onboarding.OnBoardingViewModel
import com.indieplaybook.app.presentation.screens.paywall.PaywallViewModel
import com.indieplaybook.app.presentation.screens.profile.ProfileViewModel
import com.indieplaybook.app.presentation.screens.savedbookmarks.SavedBookmarksViewModel
import com.indieplaybook.app.presentation.screens.subscriptions.SubscriptionsViewModel
import com.indieplaybook.app.presentation.screens.suggestedits.SuggestEditsViewModel
import com.indieplaybook.app.subscription.api.MockSubscriptionProvider
import com.indieplaybook.app.subscription.api.NoOpSubscriptionProviderUi
import com.indieplaybook.app.subscription.api.SubscriptionProvider
import com.indieplaybook.app.subscription.api.SubscriptionProviderFactory
import com.indieplaybook.app.subscription.api.SubscriptionProviderUi
import com.indieplaybook.app.util.ApplicationScope
import com.indieplaybook.app.util.Constants
import com.indieplaybook.app.util.defaultAsyncDispatcher
import com.indieplaybook.app.util.extensions.nowEpochMillis
import com.indieplaybook.app.util.isAndroid
import com.indieplaybook.app.util.logging.Logger
import com.indieplaybook.app.util.logging.NapierLogger
import com.indieplaybook.app.util.logging.TelegramLogger
import com.indieplaybook.app.util.platformModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

/**
 * Koin module graph for the shared app. [AppInitializer] loads [appModules] at startup.
 * Layered by concern: [domainModule] (pure, empty), [dataModule] (infra + repositories),
 * [presentationModule] (ViewModels), plus the per-target expect/actual `platformModule`.
 */

// Empty by design — the domain layer is pure (models/exceptions), nothing to inject.
private val domainModule = module {
}

// Infrastructure + repositories: scopes/dispatchers, preferences, network, auth & subscription
// providers (selected via Constants), repositories, loggers, AI provider, and the credit system.
private val dataModule = module {
    singleOf(::ApplicationScope)
    factory { defaultAsyncDispatcher } bind CoroutineContext::class
    factory { BackgroundExecutor.IO } bind BackgroundExecutor::class

    // Preferences Source. The DataStore instance stays out of the Koin graph on
    // purpose — generic types erase to `DataStore`, so a second DataStore<T>
    // registered later would silently collide with this one.
    single { UserPreferencesImpl(get<PreferencesDataStoreProvider>().providePreferencesDataStore()) } bind UserPreferences::class

    // Remote source
    single { HttpClientFactory.default(get()) }
    single(named("aiDirectClient")) { HttpClientFactory.noAuth() }
    single { AiTransport(proxyClient = get(), directClient = get(named("aiDirectClient"))) }
    single { TemporaryFileUploadApiService(HttpClientFactory.fileUpload()) }

    factoryOf(::ApiService)
    factoryOf(::OpenAiApiService)
    factoryOf(::ReplicateApiService)

    // Auth provider
    factory { AppConfiguration.authServiceProviderFactory } bind AuthServiceProviderFactory::class
    single { get<AuthServiceProviderFactory>().create() } bind AuthServiceProvider::class

    // Subscription Provider. When no real SDK key is set (isSubscriptionMockActive), swap in the
    // MockSubscriptionProvider so the paywall/purchase/unlock flow is explorable with zero keys.
    // Auto-reverts to the real provider (Adapty/RevenueCat) the moment a key is configured.
    factory { AppConfiguration.subscriptionProviderFactory } bind SubscriptionProviderFactory::class
    single {
        if (isSubscriptionMockActive()) {
            val userPreferences = get<UserPreferences>()
            MockSubscriptionProvider(
                readPremiumPurchased = {
                    userPreferences.getBoolean(MockSubscriptionProvider.KEY_MOCK_PREMIUM_PURCHASED, false)
                },
                writePremiumPurchased = {
                    userPreferences.putBoolean(MockSubscriptionProvider.KEY_MOCK_PREMIUM_PURCHASED, it)
                },
                premiumAccessId = Constants.PAYWALL_PREMIUM_ACCESS,
                creditPackPrefix = Constants.CREDIT_PACK_PRODUCT_ID_PREFIX,
                creditPackPlacementId = Constants.PAYWALL_PLACEMENT_CREDITS_PACK,
                currentTimeMillis = ::nowEpochMillis,
            )
        } else {
            get<SubscriptionProviderFactory>().createProvider()
        }
    } bind SubscriptionProvider::class
    factory {
        if (isSubscriptionMockActive()) {
            NoOpSubscriptionProviderUi
        } else {
            get<SubscriptionProviderFactory>().createProviderUi()
        }
    } bind SubscriptionProviderUi::class

    // Repositories
    single { AppStoryRepository(get()) }
    single { UserRepository(get(), get(), get(), get(), get()) }
    single { SubscriptionRepository(get(), get(), get(), get()) }
    single { GenerationRepository(get(), get(), get(), get(), get(), get(), get()) }

    // Loggers
    factory { TelegramLogger(get(), get(), get()) } bind Logger::class
    factory { NapierLogger() } bind Logger::class

//    factory<AiGenerationProvider> { OpenAiImageGenerationProvider(get(), get()) }
    factory<AiGenerationProvider> { ReplicateGenerationProvider(get(), get()) }

    initializeCreditSystem()
}

// ViewModels, scoped per NavEntry. PaywallViewModel takes a placementId param.
private val presentationModule = module {
    viewModelOf(::OnBoardingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::GalleryViewModel)
    viewModelOf(::ProfileViewModel)
    viewModel { (placementId: String?) ->
        PaywallViewModel(
            placementId = placementId,
            subscriptionRepository = get(),
            creditRepository = get(),
            userRepository = get(),
            featureFlagManager = get(),
        )
    }
    viewModelOf(::AccountViewModel)
    viewModelOf(::SubscriptionsViewModel)
    viewModelOf(::GenerationResultViewModel)
    viewModelOf(::CreditBalanceViewModel)

    viewModelOf(::HomeFeedViewModel)
    viewModelOf(::AppStoryDetailViewModel)
    viewModelOf(::SavedBookmarksViewModel)
    viewModelOf(::SuggestEditsViewModel)
    viewModelOf(::AdminEditStoryViewModel)
    viewModelOf(::AdminReviewSuggestionsViewModel)
    viewModelOf(::MethodologyViewModel)
    // Add new view models below — generate_screen.sh inserts here.
}

private fun Module.initializeCreditSystem() {
    single {
        val userPreferences = get<UserPreferences>()
        val subscriptionRepository = get<SubscriptionRepository>()
        val appCreditSystemConfig = creditSystemConfig {
            oneTimeBonus("welcome_bonus_credit", 1)
//            oneTimeBonus(
//                id = "referral_bonus",
//                amount = 1,
//                condition = {
//                    userPreferences.getBoolean(UserPreferences.KEY_REFERRAL_COMPLETED)
//                }
//            )
//            recurringWeekly(
//                id = "free_plan_weekly",
//                amount = 2,
//                condition = {
//                    !subscriptionRepository.hasPremiumAccess()
//                }
//            )

            recurringWeekly(
                id = "premium_plan_weekly", // Gives all premium users 10 credits per week
                amount = 10,
                condition = {
                    subscriptionRepository.hasPremiumAccess()
                },
            )
        }

        CreditRepository(appCreditSystemConfig, get(), get(), get(), get())
    }
}

/**
 * True when the active-platform subscription SDK key is still a placeholder, i.e. no real
 * Adapty/RevenueCat account is wired yet. While true the app runs [MockSubscriptionProvider] so the
 * whole paywall → purchase → unlock flow is explorable with zero keys. Auto-off once a real key is set.
 */
private fun isSubscriptionMockActive(): Boolean {
    val key =
        if (isAndroid) {
            BuildConfig.SUBSCRIPTION_PROVIDER_ANDROID_API_KEY
        } else {
            BuildConfig.SUBSCRIPTION_PROVIDER_IOS_API_KEY
        }
    return key.isBlank() || key == MockSubscriptionProvider.PLACEHOLDER_KEY
}

// All Koin modules loaded at startup. platformModule is the expect/actual per-target module.
internal val appModules: List<Module> get() = platformModule + domainModule + dataModule + presentationModule
