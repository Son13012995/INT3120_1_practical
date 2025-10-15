package com.example.sports.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Data model for Sport
 */
data class Sport(
    val id: Int,
    @StringRes val sportDetails: Int,
    @DrawableRes val sportsImageBanner: Int,
    @DrawableRes val imageResourceId: Int,
    val olympic: Boolean,
    val playerCount: Int,
    @StringRes val subtitleResourceId: Int,
    @StringRes val titleResourceId: Int
)