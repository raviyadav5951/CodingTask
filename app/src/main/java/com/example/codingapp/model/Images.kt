package com.example.codingapp.model

data class ImageResponse(
    val data: List<Data>?,
    val success: Boolean,
    val status: Int?
)

data class Data(
    val id: String,
    val title: String?,
    val images: List<Images>?,
)


data class Images(
    val id: String,
    var title: String?,
    val description: String?,
    val link: String?,
    val type: String?
)
