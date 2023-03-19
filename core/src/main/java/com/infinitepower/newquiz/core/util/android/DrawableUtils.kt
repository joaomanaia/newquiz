package com.infinitepower.newquiz.core.util.android

/*
/**
 * Get the dominant color of the image asynchronously
 */
fun Drawable.getDominantColor(
    onFinish: (Color) -> Unit
) {
    val bitmap = (this as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

    Palette.from(bitmap).generate { palette ->
        palette?.vibrantSwatch?.rgb?.let { colorValue ->
            onFinish(Color(colorValue))
        }
    }
}

 */