package com.app.vibespace.Enums

enum class CreateOtpType {
    UR, //User Register
    UL, //User Login
    UFP, //Forgot Password
    UU, //Update User
}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}
enum class DeviceType(key: String) {
    IOS("ios"),
}

enum class AuthType(key: String) {
    APP("app"),
    FACEBOOK("facebook"),
    GOOGLE("google"),
    APPLE("apple")

}