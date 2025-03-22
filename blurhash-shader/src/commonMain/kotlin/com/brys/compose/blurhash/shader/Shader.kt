package com.brys.compose.blurhash.shader

import androidx.compose.runtime.Stable
import com.brys.compose.blurhash.shader.decoder.BlurHashDecoder
import com.brys.compose.blurhash.shader.decoder.Size

@Stable
class ComputedColorMatrix(val size: Size, val colors: FloatArray)

// Thanks to peerless2012 (https://github.com/peerless2012/blurhash-android)
val blurhashComposeSKSL = """
        uniform vec2 startPos;
        uniform vec2 iResolution;
        uniform vec2 num;
        uniform vec4 colors[32];
        float linearTosRGB(float value) {
            float v = max(0, min(1, value));
            if (v <= 0.0031308) {
                return v * 12.92;
            } else {
                return pow(v, 1.0 / 2.4) * 1.055 - 0.055;
            }
        }
        vec4 main(float2 fragCoord) {
            vec2 uv = (fragCoord.xy - startPos.xy) / iResolution.xy;
            vec3 color = vec3(0.0);
            vec2 uvpi = uv * 3.14159265358979323846;
            int size = int(num.x * num.y);
            for (int index = 0; index < 32; index++) {
                if (index >= size) break;
                vec3 sColor = colors[index].rgb;
                float fIndex = float(index);
                float row = floor(fIndex / num.x);
                float col = floor(fIndex - (row * num.x));
                vec2 loopPos = vec2(col, row);
                vec2 basics = uvpi * loopPos;
                color += sColor * cos(basics.x) * cos(basics.y);
            }
            return vec4(linearTosRGB(color.r), linearTosRGB(color.g), linearTosRGB(color.b), 1.0);
}
    """.trimIndent()

fun calculateColorMatrix(hash: String): ComputedColorMatrix? {
    val computedColors = BlurHashDecoder.parse(hash) ?: return null

    // Each color takes up 4 numbers (r, g, b, a)
    val colorsMatrix = FloatArray(4 * 32)
    var index = 0

    for (color in computedColors.colors) {
        color.copyInto(colorsMatrix, index, startIndex = 0, endIndex = color.size)
        index += color.size
        colorsMatrix[index] = 0f
        index++
    }

    return ComputedColorMatrix(computedColors.size, colorsMatrix)
}
