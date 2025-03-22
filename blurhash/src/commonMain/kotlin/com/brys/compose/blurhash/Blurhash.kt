package com.brys.compose.blurhash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 *  Creates a shader backed composable that displays the given blurhash string
 *
 *  - Desktop - Working
 *  - WasmJS - Working
 *  - iOS - Working
 *  - Android - **NOT WORKING**
 *
 */
@Composable
expect fun BlurHashImage(hash: String, modifier: Modifier = Modifier)