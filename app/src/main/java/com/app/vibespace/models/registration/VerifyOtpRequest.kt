package com.app.vibespace.models.registration

data class VerifyOtpRequest(
    var email: String? = null,
    var otp: Int = 0,
    var type: String = "",
)
