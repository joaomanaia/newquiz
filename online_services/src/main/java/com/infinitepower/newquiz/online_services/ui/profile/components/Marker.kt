package com.infinitepower.newquiz.online_services.ui.profile.components

import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatryk.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatryk.vico.core.chart.insets.Insets
import com.patrykandpatryk.vico.core.component.OverlayingComponent
import com.patrykandpatryk.vico.core.component.marker.MarkerComponent
import com.patrykandpatryk.vico.core.component.shape.DashedShape
import com.patrykandpatryk.vico.core.component.shape.LineComponent
import com.patrykandpatryk.vico.core.component.shape.ShapeComponent
import com.patrykandpatryk.vico.core.component.shape.Shapes.pillShape
import com.patrykandpatryk.vico.core.component.shape.cornered.Corner
import com.patrykandpatryk.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatryk.vico.core.component.text.textComponent
import com.patrykandpatryk.vico.core.context.MeasureContext
import com.patrykandpatryk.vico.core.dimensions.MutableDimensions
import com.patrykandpatryk.vico.core.extension.copyColor
import com.patrykandpatryk.vico.core.marker.Marker

internal fun getMarker(
    @ColorInt labelColor: Int,
    @ColorInt bubbleColor: Int,
    @ColorInt indicatorInnerColor: Int,
    @ColorInt guidelineColor: Int,
): Marker {

    val labelBackgroundShape = MarkerCorneredShape(all = Corner.FullyRounded)
    val label = textComponent {
        color = labelColor
        ellipsize = TextUtils.TruncateAt.END
        lineCount = 1
        padding = MutableDimensions(horizontalDp = 8f, verticalDp = 4f)
        typeface = Typeface.MONOSPACE
        background = ShapeComponent(shape = labelBackgroundShape, color = bubbleColor)
            .setShadow(radius = SHADOW_RADIUS, dy = SHADOW_DY, applyElevationOverlay = true)
    }

    val indicatorInner = ShapeComponent(shape = pillShape, color = indicatorInnerColor)
    val indicatorCenter = ShapeComponent(shape = pillShape, color = Color.WHITE)
    val indicatorOuter = ShapeComponent(shape = pillShape, color = Color.WHITE)

    val indicator = OverlayingComponent(
        outer = indicatorOuter,
        innerPaddingAllDp = 10f,
        inner = OverlayingComponent(
            outer = indicatorCenter,
            inner = indicatorInner,
            innerPaddingAllDp = 5f,
        ),
    )

    val guideline = LineComponent(
        color = guidelineColor.copyColor(alpha = GUIDELINE_ALPHA),
        thicknessDp = 2f,
        shape = DashedShape(
            shape = pillShape,
            dashLengthDp = 8f,
            gapLengthDp = 4f,
        ),
    )

    return object : MarkerComponent(
        label = label,
        indicator = indicator,
        guideline = guideline,
    ) {
        init {
            indicatorSizeDp = INDICATOR_SIZE_DP
            onApplyEntryColor = { entryColor ->
                indicatorOuter.color = entryColor.copyColor(alpha = 32)
                with(indicatorCenter) {
                    color = entryColor
                    setShadow(radius = 12f, color = entryColor)
                }
            }
        }

        override fun getInsets(
            context: MeasureContext,
            outInsets: Insets,
            horizontalDimensions: HorizontalDimensions
        ) = with(context) {
            outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
                    SHADOW_RADIUS.pixels * SHADOW_RADIUS_TO_PX_MULTIPLIER - SHADOW_DY.pixels
        }
    }
}

@Composable
internal fun marker(): Marker = with(MaterialTheme.colorScheme) {
    getMarker(
        labelColor = onSurface.toArgb(),
        bubbleColor = surface.toArgb(),
        indicatorInnerColor = surface.toArgb(),
        guidelineColor = onSurface.toArgb(),
    )
}

private const val SHADOW_RADIUS = 4f
private const val SHADOW_RADIUS_TO_PX_MULTIPLIER = 1.3f
private const val SHADOW_DY = 2f
private const val GUIDELINE_ALPHA = 0.2f
private const val INDICATOR_SIZE_DP = 36f