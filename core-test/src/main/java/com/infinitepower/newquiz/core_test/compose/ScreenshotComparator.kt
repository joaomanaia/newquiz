package com.infinitepower.newquiz.core_test.compose

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.io.FileOutputStream

/**
 * Simple on-device screenshot comparator that uses golden images present in
 * `androidTest/assets`,
 *
 * Minimum SDK is O. Densities between devices must match.
 *
 * Screenshots are saved on device in `/data/data/{package}/files`.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun SemanticsNodeInteraction.assertMatchesGolden(
    goldenName: String,
    folderPath: String? = null
) {
    val bitmap = captureToImage().asAndroidBitmap()

    // Save screenshot to file for debugging
    saveScreenshot(goldenName + System.currentTimeMillis().toString(), bitmap)

    val fileName = if (folderPath == null) "$goldenName.png" else "$folderPath/$goldenName.png"

    val golden = InstrumentationRegistry
        .getInstrumentation()
        .context
        .resources
        .assets
        .open(fileName)
        .use { BitmapFactory.decodeStream(it) }

    golden.compare(bitmap)
}

private fun saveScreenshot(filename: String, bmp: Bitmap) {
    val path = InstrumentationRegistry.getInstrumentation().targetContext.filesDir.canonicalPath
    FileOutputStream("$path/$filename.png").use { out ->
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    println("Saved screenshot to $path/$filename.png")
}

private fun Bitmap.compare(other: Bitmap) {
    if (this.width != other.width || this.height != other.height) {
        throw AssertionError("Size of screenshot does not match golden file (check device density)")
    }
    // Compare row by row to save memory on device
    val row1 = IntArray(width)
    val row2 = IntArray(width)
    for (column in 0 until height) {
        // Read one row per bitmap and compare
        this.getRow(row1, column)
        other.getRow(row2, column)
        if (!row1.contentEquals(row2)) {
            throw AssertionError("Sizes match but bitmap content has differences")
        }
    }
}

private fun Bitmap.getRow(pixels: IntArray, column: Int) {
    this.getPixels(pixels, 0, width, 0, column, width, 1)
}

fun clearExistingImages(folderName: String) {
    val path = File(InstrumentationRegistry.getInstrumentation().targetContext.filesDir, folderName)
    path.deleteRecursively()
}
