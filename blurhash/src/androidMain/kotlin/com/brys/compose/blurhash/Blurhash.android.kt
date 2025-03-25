package com.brys.compose.blurhash

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush
import com.brys.compose.blurhash.shader.blurhashComposeSKSL
import com.brys.compose.blurhash.shader.calculateColorMatrix

/**
 * **THIS CURRENTLY DOES NOT WORK FOR ANDROID DEVICES!**
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
actual fun BlurHashImage(hash: String, modifier: Modifier) {
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
            }
            .then(modifier)
    )
}
