package com.indieplaybook.app.util

import com.indieplaybook.app.data.source.featureflag.FeatureFlagManager
import com.indieplaybook.app.presentation.components.ads.AdsManager
import com.indieplaybook.app.presentation.components.ads.IosAdsDisplayer
import com.indieplaybook.app.util.analytics.Analytics

/**
This factory is used to help to use swift libraries in KMP. Actual implementations are provided in swift.
 */
interface SwiftLibDependencyFactory {
    fun provideFeatureFlagManagerImpl(): FeatureFlagManager
    fun provideFirebaseAnalyticsImpl(): Analytics
    fun provideAdsManagerImpl(): AdsManager
    fun provideIosAdsDisplayer(): IosAdsDisplayer
}
