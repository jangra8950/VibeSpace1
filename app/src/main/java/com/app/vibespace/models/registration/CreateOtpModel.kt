package com.app.vibespace.models.registration

data class CreateOtpModel (
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
) {
    data class Data(
        val message: String
    )
}
