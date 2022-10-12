package com.example.jetpack.demo.data.api.model

import com.google.gson.annotations.SerializedName

data class DemoResult<T>(
    @SerializedName("code") var code: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: T
)

data class ImageData(
    val images: List<String>,
    val total: Int,
    val page: Int,
    val pages: Int
)
