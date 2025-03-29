package com.brys.compose.blurhash.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brys.compose.blurhash.BlurHashImage

@Composable
fun Sample() {
    Box(modifier = Modifier.fillMaxSize()) {
        BlurHashImage(
            "URGtW@NM}FV?w7={wOi{xYi%s,sm-CNZnPxt",
            contentDescription = "Hi",
            modifier = Modifier.fillMaxSize()
        )
    }
}