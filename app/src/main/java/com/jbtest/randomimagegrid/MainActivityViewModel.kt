package com.jbtest.randomimagegrid

import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import com.jbtest.randomimagegrid.data.RandomImageRepository
import com.jbtest.randomimagegrid.domain.ImageRepository

class MainActivityViewModel constructor(
    private val imageRepository: ImageRepository = RandomImageRepository(),
) : ViewModel() {
    fun getTileUrl(tile: IntOffset): String = imageRepository.getTileImage(tile).url
}
