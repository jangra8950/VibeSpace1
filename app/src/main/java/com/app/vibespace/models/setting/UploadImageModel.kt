package com.app.vibespace.models.setting

data class UploadImageModel(
    val data: Data,
    val message: String,
    val statusCode: Int
) {
    data class Data(
        val url: String
    )
}
