package com.brys.compose.blurhash

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.RuntimeShader
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.brys.compose.blurhash.decoder.decode
import com.brys.compose.blurhash.shader.blurhashComposeSKSL
import com.brys.compose.blurhash.shader.calculateColorMatrix
import com.brys.compose.blurhash.shader.decoder.BlurHashDecoder

@SuppressLint("NewApi")
@Composable
actual fun BlurHashImage(hash: String, contentDescription: String, modifier: Modifier) {
    when {
        // SDK >= 33 we can use AGSL for blurhash
        Build.VERSION.SDK_INT >= TIRAMISU -> ShaderBasedBlurhash(hash, contentDescription, modifier)
        // Fallback to bitmap based blurhash when sdk < 33
        else -> BitmapBasedBlurhash(hash, contentDescription, modifier)
    }
}

@RequiresApi(TIRAMISU)
@Composable
fun ShaderBasedBlurhash(hash: String, contentDescription: String, modifier: Modifier) {
    val compositeRuntimeEffect = RuntimeShader(blurhashComposeSKSL)
    val computedMatrix = remember { calculateColorMatrix(hash) } ?: return
    compositeRuntimeEffect.setFloatUniform(
        "num",
        computedMatrix.size.width.toFloat(),
        computedMatrix.size.height.toFloat()
    )

    compositeRuntimeEffect.setFloatUniform("colors", computedMatrix.colors)
    compositeRuntimeEffect.setFloatUniform("startPos", 0f, 0f)

    Box(
        modifier = Modifier
            .drawWithCache {
                compositeRuntimeEffect.setFloatUniform("iResolution", this.size.width, this.size.height)
                val brush = ShaderBrush(compositeRuntimeEffect)

                onDrawBehind {
                    drawRect(brush, size = Size(this.size.width, this.size.height))
                }
            }.semantics {
                this.contentDescription = contentDescription
            }
            .then(modifier)
    )
}

@Composable
fun BitmapBasedBlurhash(hash: String, contentDescription: String, modifier: Modifier) {
    val bitmap = remember { BlurHashDecoder.decode(hash, 32, 32) }

    Image(
        bitmap = bitmap?.asImageBitmap()!!,
        contentDescription = contentDescription,
        modifier = modifier,
        // Consider making this configurable later on
        contentScale = ContentScale.Crop
    )
}

