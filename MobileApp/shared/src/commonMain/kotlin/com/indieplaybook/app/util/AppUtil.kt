package com.indieplaybook.app.util

// Platform-specific app utilities (name, version, share sheet, feedback email). actual per target.
interface AppUtil {
    fun getAppName(): String
    fun shareApp()
    fun openFeedbackMail()
    fun getAppVersionInfo(): String
}
