package com.app.vibespace.util

object ApiConstants {

    const val BASE_URL = "https://apivibespace.zimblecode.com/api/"
    // Post calls
    const val API_POST_CREATE_OTP = "otp/create"
    const val API_POST_VERIFY_OTP = "otp/verify"
    const val API_POST_USER="user"
    const val API_POST_LOGIN_USER= "user/login"
    const val API_POST_LOGOUT="user/logout"
    const val API_POST_RESET_PASSWORD="user/password/reset/email"


    // Get call
    const val API_GET_UNIVERSITY_LIST="university"
    const val API_GET_PROFILE="user/profile"


    // Delete call
    const val API_DELETE_ACCOUNT="user/64a54428038d3bba08c818c2"

    // update call
    const val API_UPDATE_USER="user"


    const val Profile_pic="https://i.pravatar.cc/150?img=3"
    const val Device_Token="1234567890"
    const val Timer_Value=60000
    var IS_SLIDE_ALREADY = false

    const val PREFERENCE_NAME = "pref_vibe_spa_ce"
    const val PREF_AUTH_TOKEN="vibe_spa_ce"

    const val SHARED_PREFS = "shared_prefs"
    const val PROFILE_DATA = "profileData"


}