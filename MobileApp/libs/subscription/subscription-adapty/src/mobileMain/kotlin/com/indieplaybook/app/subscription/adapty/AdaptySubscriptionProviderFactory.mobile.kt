package com.indieplaybook.app.subscription.adapty

import com.indieplaybook.app.subscription.api.SubscriptionProvider
import com.indieplaybook.app.subscription.api.SubscriptionProviderFactory
import com.indieplaybook.app.subscription.api.SubscriptionProviderUi

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() =
        object : SubscriptionProviderFactory {
            override fun createProvider(): SubscriptionProvider = AdaptySubscriptionProvider()

            override fun createProviderUi(): SubscriptionProviderUi = AdaptySubscriptionProviderUi()
        }
