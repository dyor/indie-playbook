package com.indieplaybook.app.subscription.revenuecat

import com.indieplaybook.app.subscription.api.SubscriptionProvider
import com.indieplaybook.app.subscription.api.SubscriptionProviderFactory
import com.indieplaybook.app.subscription.api.SubscriptionProviderUi

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() =
        object : SubscriptionProviderFactory {
            override fun createProvider(): SubscriptionProvider = RevenueCatSubscriptionProvider()

            override fun createProviderUi(): SubscriptionProviderUi = RevenueCatSubscriptionProviderUi()
        }
