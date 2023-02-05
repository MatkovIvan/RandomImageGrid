package com.jbtest.randomimagegrid.ui.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import kotlin.math.roundToInt

/**
 * Infinite grid of fixed size tiles.
 * Supports drag and scale.
 *
 * TODO: mouse wheel
 * TODO: inertia
 * TODO: animateScrollTo
 * TODO: preload tiles around
 */
@Composable
fun LazyInfiniteGrid(
    itemWidth: Dp,
    itemHeight: Dp,
    modifier: Modifier = Modifier,
    state: LazyInfiniteGridState = rememberLazyInfiniteGridState(),
    zoomLimit: ClosedFloatingPointRange<Float> = 0.2f..5.0f,
    itemContent: @Composable (IntOffset) -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
            .scale(state.scale)
            .clipToBounds()
            .lazyInfiniteGridInput(state, zoomLimit)
    ) { constraints ->
        val gridWidth = (constraints.maxWidth / state.scale).roundToInt()
        val gridHeight = (constraints.maxHeight / state.scale).roundToInt()
        val visibleBounds = with(state.offset) { IntRect(x, y, x + gridWidth, y + gridHeight) }
        layout(gridWidth, gridHeight) {
            val itemWidthPx = itemWidth.toPx().roundToInt()
            val itemHeightPx = itemHeight.toPx().roundToInt()
            tilesInBounds(visibleBounds, itemWidthPx, itemHeightPx).forEach {
                // Use tile coordinates as slotId to cache compose calls.
                subcompose(slotId = it) @Composable { itemContent(it) }.forEach { measurable ->
                    val x = it.x * itemWidthPx - state.offset.x
                    val y = it.y * itemHeightPx - state.offset.y
                    measurable.measure(Constraints(
                        maxWidth = itemWidthPx,
                        maxHeight = itemHeightPx
                    )).placeRelative(x, y)
                }
            }
        }
    }
}

@Stable
class LazyInfiniteGridState {
    private val _offset = mutableStateOf(Offset(0f, 0f))
    private val _scale = mutableStateOf(1.0f)

    val offset
        get() = IntOffset(
            _offset.value.x.roundToInt(),
            _offset.value.y.roundToInt()
        )
    val scale
        get() = _scale.value

    internal fun onTransformGestures(centroid: Offset, pan: Offset, zoom: Float, zoomLimit: ClosedFloatingPointRange<Float>) {
        _offset.value -= pan

        // For natural zooming, the centroid of the gesture should affect point where zooming occurs.
        val newScale = (_scale.value * zoom).coerceIn(zoomLimit)
        val limitedZoom = newScale / _scale.value
        _offset.value += centroid - centroid / limitedZoom
        _scale.value = newScale
    }
}

private fun Modifier.lazyInfiniteGridInput(state: LazyInfiniteGridState, zoomLimit: ClosedFloatingPointRange<Float>): Modifier = pointerInput(Unit) {
    detectTransformGestures { centroid, pan, zoom, _ -> state.onTransformGestures(centroid, pan, zoom, zoomLimit) }
}

private fun tilesInBounds(bounds: IntRect, itemWidth: Int, itemHeight: Int) = sequence {
    val xRange = bounds.left.floorDiv(itemWidth)..bounds.right.floorDiv(itemWidth)
    val yRange = bounds.top.floorDiv(itemHeight)..bounds.bottom.floorDiv(itemHeight)
    for (x in xRange) {
        for (y in yRange) {
            yield(IntOffset(x, y))
        }
    }
}

@Composable
fun rememberLazyInfiniteGridState(): LazyInfiniteGridState = remember {
    LazyInfiniteGridState()
}
