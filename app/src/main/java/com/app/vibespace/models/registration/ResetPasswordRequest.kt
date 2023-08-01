package com.app.vibespace.models.registration

data class ResetPasswordRequest(
    var confirmPassword: String?=null,
    var email: String?=null,
    var newPassword: String?=null,
    var otpToken: Int?=null
)