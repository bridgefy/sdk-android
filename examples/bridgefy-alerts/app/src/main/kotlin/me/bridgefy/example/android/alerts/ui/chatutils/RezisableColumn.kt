package me.bridgefy.example.android.alerts.ui.chatutils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize

@Composable
fun SubcomposeColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    SubcomposeLayout(modifier = modifier) { constraints ->

        var recompositionIndex = 0

        var placeables: List<Placeable> = subcompose(recompositionIndex++, content).map {
            it.measure(constraints)
        }

        val columnSize =
            placeables.fold(IntSize.Zero) { currentMax: IntSize, placeable: Placeable ->
                IntSize(
                    width = maxOf(currentMax.width, placeable.width),
                    height = currentMax.height + placeable.height,
                )
            }

        if (placeables.isNotEmpty() && placeables.size > 1) {
            placeables = subcompose(recompositionIndex, content).map { measurable: Measurable ->
                measurable.measure(Constraints(columnSize.width, constraints.maxWidth))
            }
        }

        layout(columnSize.width, columnSize.height) {
            var yPos = 0
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(0, yPos)
                yPos += placeable.height
            }
        }
    }
}

@Composable
fun SubcomposeColumn(
    modifier: Modifier = Modifier,
    mainContent: @Composable () -> Unit = {},
    dependentContent: @Composable (IntSize) -> Unit,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->

        var recompositionIndex = 0

        var mainPlaceables: List<Placeable> = subcompose(recompositionIndex++, mainContent).map {
            it.measure(constraints)
        }

        var columnSize =
            mainPlaceables.fold(IntSize.Zero) { currentMax: IntSize, placeable: Placeable ->
                IntSize(
                    width = maxOf(currentMax.width, placeable.width),
                    height = currentMax.height + placeable.height,
                )
            }

        val dependentMeasurables: List<Measurable> = subcompose(recompositionIndex++) {
            dependentContent(columnSize)
        }

        val dependentPlaceables: List<Placeable> = dependentMeasurables
            .map { measurable: Measurable ->
                measurable.measure(Constraints(columnSize.width, constraints.maxWidth))
            }

        val maxWidth = if (!dependentPlaceables.isEmpty()) {
            dependentPlaceables.maxOf { it.width }
        } else {
            columnSize.width
        }

        if (mainPlaceables.isNotEmpty() && maxWidth > columnSize.width) {
            mainPlaceables = subcompose(recompositionIndex, mainContent).map {
                it.measure(Constraints(maxWidth, constraints.maxWidth))
            }
        }

        if (dependentPlaceables.isNotEmpty()) {
            columnSize = IntSize(
                columnSize.width.coerceAtLeast(maxWidth),
                columnSize.height + dependentPlaceables.sumOf { it.height },
            )
        }

        layout(columnSize.width, columnSize.height) {
            var posY = 0

            if (mainPlaceables.isNotEmpty()) {
                mainPlaceables.forEach {
                    it.placeRelative(0, posY)
                    posY += it.height
                }
            }
            if (dependentPlaceables.isNotEmpty()) {
                dependentPlaceables.forEach {
                    it.placeRelative(0, posY)
                    posY += it.height
                }
            }
        }
    }
}
