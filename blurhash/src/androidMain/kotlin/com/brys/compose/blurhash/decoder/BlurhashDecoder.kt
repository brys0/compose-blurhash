package com.brys.compose.blurhash.decoder

import android.graphics.Bitmap
import android.graphics.Color
import com.brys.compose.blurhash.shader.decoder.BlurHashDecoder
import kotlin.math.cos
import kotlin.math.pow

// Contains function to generate bitmap backed blurhash for Android
// Again this code was provided by https://github.com/woltapp/blurhash

// cache Math.cos() calculations to improve performance.
// The number of calculations can be huge for many bitmaps: width * height * numCompX * numCompY * 2 * nBitmaps
// the cache is enabled by default, it is recommended to disable it only when just a few images are displayed
private val cacheCosinesX: HashMap<Int, DoubleArray> = HashMap()
private val cacheCosinesY: HashMap<Int, DoubleArray> = HashMap()

fun BlurHashDecoder.decode(blurHash: String?, width: Int, height: Int, punch: Float = 1f, useCache: Boolean = true): Bitmap? {
    val computedColorMatrix = parse(blurHash, punch) ?: return null
    val size = computedColorMatrix.size
    val colors = computedColorMatrix.colors
    return composeBitmap(width, height, size.width, size.height, colors, useCache)
}

private fun getArrayForCosinesY(calculate: Boolean, height: Int, numCompY: Int) = when {
    calculate -> {
        DoubleArray(height * numCompY).also {
            cacheCosinesY[height * numCompY] = it
        }
    }
    else -> {
        cacheCosinesY[height * numCompY]!!
    }
}

private fun getArrayForCosinesX(calculate: Boolean, width: Int, numCompX: Int) = when {
    calculate -> {
        DoubleArray(width * numCompX).also {
            cacheCosinesX[width * numCompX] = it
        }
    }
    else -> cacheCosinesX[width * numCompX]!!
}

private fun DoubleArray.getCos(
    calculate: Boolean,
    x: Int,
    numComp: Int,
    y: Int,
    size: Int
): Double {
    if (calculate) {
        this[x + numComp * y] = cos(Math.PI * y * x / size)
    }
    return this[x + numComp * y]
}

private fun linearToSrgb(value: Float): Int {
    val v = value.coerceIn(0f, 1f)
    return if (v <= 0.0031308f) {
        (v * 12.92f * 255f + 0.5f).toInt()
    } else {
        ((1.055f * v.pow(1 / 2.4f) - 0.055f) * 255 + 0.5f).toInt()
    }
}

private fun BlurHashDecoder.composeBitmap(
    width: Int, height: Int,
    numCompX: Int, numCompY: Int,
    colors: Array<FloatArray>,
    useCache: Boolean
): Bitmap {
    // use an array for better performance when writing pixel colors
    val imageArray = IntArray(width * height)
    val calculateCosX = !useCache || !cacheCosinesX.containsKey(width * numCompX)
    val cosinesX = getArrayForCosinesX(calculateCosX, width, numCompX)
    val calculateCosY = !useCache || !cacheCosinesY.containsKey(height * numCompY)
    val cosinesY = getArrayForCosinesY(calculateCosY, height, numCompY)
    for (y in 0 until height) {
        for (x in 0 until width) {
            var r = 0f
            var g = 0f
            var b = 0f
            for (j in 0 until numCompY) {
                for (i in 0 until numCompX) {
                    val cosX = cosinesX.getCos(calculateCosX, i, numCompX, x, width)
                    val cosY = cosinesY.getCos(calculateCosY, j, numCompY, y, height)
                    val basis = (cosX * cosY).toFloat()
                    val color = colors[j * numCompX + i]
                    r += color[0] * basis
                    g += color[1] * basis
                    b += color[2] * basis
                }
            }
            imageArray[x + width * y] = Color.rgb(linearToSrgb(r), linearToSrgb(g), linearToSrgb(b))
        }
    }
    return Bitmap.createBitmap(imageArray, width, height, Bitmap.Config.ARGB_8888)
}