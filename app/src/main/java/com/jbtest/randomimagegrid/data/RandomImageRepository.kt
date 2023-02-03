package com.jbtest.randomimagegrid.data

import androidx.compose.ui.unit.IntOffset
import com.jbtest.randomimagegrid.domain.ImageEntity
import com.jbtest.randomimagegrid.domain.ImageRepository

class RandomImageRepository: ImageRepository {
    companion object {
        const val IMAGE_SIZE = 200
    }

    override fun getTileImage(tile: IntOffset): ImageEntity =
        ImageEntity(url = "https://picsum.photos/seed/${getTileSeed(tile)}/$IMAGE_SIZE")

    private fun getTileSeed(tile: IntOffset): String =
        "${tile.x}x${tile.y}".hashCode().toString()
}