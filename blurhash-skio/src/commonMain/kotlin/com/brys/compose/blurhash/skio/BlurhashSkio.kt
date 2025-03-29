package com.brys.compose.blurhash.skio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import com.brys.compose.blurhash.shader.blurhashComposeSKSL
import com.brys.compose.blurhash.shader.calculateColorMatrix
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder

@Composable
fun CreateImageBlurHash(hash: String, contentDescription: String, modifier: Modifier = Modifier) {
    val compositeRuntimeEffect = RuntimeEffect.makeForShader(blurhashComposeSKSL)
    val compositeRuntimeBuilder = RuntimeShaderBuilder(compositeRuntimeEffect)
    val computedMatrix = remember { calculateColorMatrix(hash) } ?: return

    compositeRuntimeBuilder.uniform(
        "num",
        computedMatrix.size.height.toFloat(),
        computedMatrix.size.width.toFloat()
    )
    compositeRuntimeBuilder.uniform("colors", computedMatrix.colors)
    compositeRuntimeBuilder.uniform("startPos", 0f, 0f)

    Box(
        modifier = Modifier
            .drawWithCache {
                compositeRuntimeBuilder.uniform("iResolution", this.size.width, this.size.height)
                val brush = ShaderBrush(compositeRuntimeBuilder.makeShader())

                onDrawBehind {
                    drawRect(brush, size = Size(this.size.width, this.size.height))
                }

            }
            .then(modifier)
    )
}