package com.app.vibespace.models.registration

data class SignInRequest(
    var deviceToken: String?=null,
    var email: String?=null,
    var password: String?=null
)