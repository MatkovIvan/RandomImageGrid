package com.jbtest.randomimagegrid.domain

import androidx.compose.ui.unit.IntOffset

interface ImageRepository {
    fun getTileImage(tile: IntOffset): ImageEntity
}