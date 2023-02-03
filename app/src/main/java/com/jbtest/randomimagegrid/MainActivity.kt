package com.jbtest.randomimagegrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jbtest.randomimagegrid.ui.components.LazyInfiniteGrid
import com.jbtest.randomimagegrid.ui.theme.RandomImageGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomImageGridTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RandomImageGrid()
                }
            }
        }
    }
}

@Composable
fun RandomImageGrid() {
    LazyInfiniteGrid(
        itemWidth = 200.dp,
        itemHeight = 200.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        val seed = "${it.x}x${it.y}".hashCode().toString()
        AsyncImage(
            model = "https://picsum.photos/seed/$seed/200",
            modifier = Modifier.size(200.dp),
            contentDescription = null
        )
    }
}
