package com.brys.compose.blurhash

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
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
        computedMatrix.size.height.toFloat(),
        computedMatrix.size.width.toFloat()
    )

    compositeRuntimeEffect.setFloatUniform("colors", computedMatrix.colors)
    compositeRuntimeEffect.setFloatUniform("startPos", 0f, 0f)

    Box(
        modifier = Modifier.background(Color.Red)
            .onSizeChanged {
                compositeRuntimeEffect.setFloatUniform("iResolution", it.width.toFloat(), it.height.toFloat())
            }
            .graphicsLayer {
                compositeRuntimeEffect.setFloatUniform("iResolution", this.size.width, this.size.height)
                this.renderEffect = RenderEffect.createShaderEffect(
                    compositeRuntimeEffect,
                ).asComposeRenderEffect()
            }
            .then(modifier)
    )
}