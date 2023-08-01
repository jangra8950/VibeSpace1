package com.app.vibespace.models.registration

data class SignUpRequest(
    var authType: String?=null,
    var bio: String?=null,
    var confirmPassword: String?=null,
    var deviceToken: String?=null,
    var deviceType: String?=null,
    var dob: String?=null,
    var email: String?=null,
    var firstName: String?=null,
    var lastName: String?=null,
    var otpToken: Int?=null,
    var password: String?=null,
    var profilePic: String?=null,
    var universityId: String?=null
)


