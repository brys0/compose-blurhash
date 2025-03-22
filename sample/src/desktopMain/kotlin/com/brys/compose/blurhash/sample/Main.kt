package com.brys.compose.blurhash.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose blurhash"
    ) {
        Sample()
    }
}