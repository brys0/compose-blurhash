package com.brys.compose.blurhash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 *  Creates a shader backed composable that displays the given blurhash string
 *
 *  - Desktop - Working
 *  - WasmJS - Working
 *  - iOS - Working
 *  - Android - Working
 *
 */
@Composable
expect fun BlurHashImage(hash: String, contentDescription: String, modifier: Modifier = Modifier)