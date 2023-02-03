package com.jbtest.randomimagegrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    RandomImageGridScreen()
                }
            }
        }
    }
}
@Composable
private fun RandomImageGridScreen(
    conversationViewModel: MainActivityViewModel = viewModel()
) {
    ImageGrid(conversationViewModel::getTileUrl)
}

@Composable
fun ImageGrid(tileImage: (IntOffset) -> String) {
    LazyInfiniteGrid(
        itemWidth = 200.dp,
        itemHeight = 200.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        val tileDescription = "${it.x}x${it.y}"
        AsyncImage(
            model = tileImage(it),
            modifier = Modifier.size(200.dp),
            placeholder = painterResource(id = android.R.drawable.ic_menu_camera),
            contentDescription = tileDescription
        )

        // Draw frame and tile number (optional).
        Box(
            modifier = Modifier
                .size(200.dp)
                .border(1.dp, Color.Black)
        ) {
            Text(
                text = tileDescription,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 30.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomImageGridTheme {
        RandomImageGridScreen()
    }
}

