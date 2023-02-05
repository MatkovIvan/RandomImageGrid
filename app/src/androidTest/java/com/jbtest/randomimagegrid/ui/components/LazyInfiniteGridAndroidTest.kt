package com.jbtest.randomimagegrid.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LazyInfiniteGridAndroidTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setUp() {
        rule.setContent {
            LazyInfiniteGrid(
                itemWidth = 100.dp,
                itemHeight = 100.dp,
                modifier = Modifier.size(250.dp)
            ) {
                // Add some real elements to be able visually debug on breakpoints.
                val tileDescription = "${it.x}x${it.y}"
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, Color.Black)
                        .testTag(tileDescription)
                ) {
                    Text(
                        text = tileDescription,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 30.sp
                    )
                }
            }
        }
    }

    @Test
    fun testInitialState() {
        rule.onRoot().onChildren().assertCountEquals(9)
        rule.onNodeWithTag("0x0").assertIsDisplayed()
        rule.onNodeWithTag("3x3").assertDoesNotExist()
    }

    @Test
    fun testDrag() {
        // Drag layout by (+60dp;+60dp) to load one more column and row.
        rule.onRoot().performTouchInput {
            swipe(
                start = Offset(160.dp.toPx(), 160.dp.toPx()),
                end = Offset(100.dp.toPx(), 100.dp.toPx())
            )
        }
        rule.onRoot().onChildren().assertCountEquals(16)
        rule.onNodeWithTag("0x0").assertIsDisplayed()
        rule.onNodeWithTag("3x3").assertIsDisplayed()

        // Drag layout by (+50dp;+50dp) to unload first more column and row.
        rule.onRoot().performTouchInput {
            swipe(
                start = Offset(150.dp.toPx(), 150.dp.toPx()),
                end = Offset(100.dp.toPx(), 100.dp.toPx())
            )
        }
        rule.onRoot().onChildren().assertCountEquals(9)
        rule.onNodeWithTag("0x0").assertDoesNotExist()
        rule.onNodeWithTag("3x3").assertIsDisplayed()
    }

    @Test
    fun testZoom() {
        // Zoom out (x3) around center.
        rule.onRoot().performTouchInput {
            pinch(
                start0 = Offset(50.dp.toPx(), centerY),
                end0 = Offset(100.dp.toPx(), centerY),
                start1 = Offset(200.dp.toPx(), centerY),
                end1 = Offset(150.dp.toPx(), centerY),
                durationMillis = 1 // Use minimal time to reduce accumulating floating error.
            )
        }
        rule.onRoot().onChildren().assertCountEquals(81)

        // FIXME: assertIsDisplayed() doesn't work properly inside scaled layouts. https://issuetracker.google.com/issues/267874118
        // rule.onNodeWithTag("-3x-3").assertIsDisplayed()
        rule.onNodeWithTag("5x5").assertIsDisplayed()
    }
}
