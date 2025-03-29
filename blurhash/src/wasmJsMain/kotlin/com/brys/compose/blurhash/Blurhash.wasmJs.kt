package com.brys.compose.blurhash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun BlurHashImage(hash: String, contentDescription: String, modifier: Modifier) = com.brys.compose.blurhash.skio.CreateImageBlurHash(hash, contentDescription, modifier)
