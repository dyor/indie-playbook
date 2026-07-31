package com.indieplaybook.app.auth.firebase

import com.indieplaybook.app.auth.api.AuthServiceProviderFactory

internal actual val authServiceProviderFactory: AuthServiceProviderFactory
    get() = AuthServiceProviderFactory { NoOpAuthServiceProvider() }
