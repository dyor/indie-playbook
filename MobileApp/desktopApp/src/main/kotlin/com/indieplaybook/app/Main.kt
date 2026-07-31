package com.indieplaybook.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.indieplaybook.app.root.App
import com.indieplaybook.app.root.AppInitializer

fun main() = application {
    AppInitializer.initialize {}
    Window(
        onCloseRequest = ::exitApplication,
        title = "IndiePlaybook",
    ) {
        App()
    }
}
