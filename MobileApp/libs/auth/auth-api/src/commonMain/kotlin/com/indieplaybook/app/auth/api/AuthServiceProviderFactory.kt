package com.indieplaybook.app.auth.api

fun interface AuthServiceProviderFactory {
    companion object {}

    fun create(): AuthServiceProvider
}
