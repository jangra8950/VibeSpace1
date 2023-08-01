package com.app.vibespace.models.registration

data class CreateOtpRequest(
    var email: String? = null,
    var type: String = "",
)
