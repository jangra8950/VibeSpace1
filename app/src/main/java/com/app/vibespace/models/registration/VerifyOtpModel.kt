package com.app.vibespace.models.registration

data class VerifyOtpModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
) {
    data class Data(
        val token: Int,
        val type: String
    )
}
