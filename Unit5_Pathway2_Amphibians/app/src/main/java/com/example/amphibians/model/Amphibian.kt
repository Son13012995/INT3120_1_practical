package com.example.amphibians.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Amphibian(
    val name: String,
    val type: String,
    val description: String,
    @SerialName("img_src") // Ánh xạ key "img_src" trong JSON vào biến imgSrc
    val imgSrc: String
)