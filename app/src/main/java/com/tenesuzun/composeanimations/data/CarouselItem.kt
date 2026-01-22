package com.tenesuzun.composeanimations.data

import androidx.annotation.DrawableRes

data class CarouselItem(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int
)
