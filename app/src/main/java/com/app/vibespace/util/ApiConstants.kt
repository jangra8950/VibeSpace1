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
    const val API_POST_CREATE_POST="post"
    const val API_POST_BLOCK_USER="blockUser"
    const val API_POST_CHANGE_PASS="user/password/change"
    const val API_POST_LIKE="like"
    const val API_POST_REPORT="reportPost"
    const val API_POST_COMMENT="comment"
    const val API_POST_CONNECT="connect"



    // Get call
    const val API_GET_UNIVERSITY_LIST="university"
    const val API_GET_PROFILE="user/profile"
    const val API_GET_POST_LIST="post/list"
    const val API_GET_BLOCKED_USER="blockUser/list"
    const val API_GET_USER_LIST="user/userList"
    const val API_GET_COMMENT_LIST="comment/list"
    const val API_GET_PEOPLE="user/people"
    const val API_GET_SUMMARY="chats/summary"
    const val API_GET_CHAT_IND="chats/individual"

    // Delete call
    const val API_DELETE_ACCOUNT="user/64a54428038d3bba08c818c2"
    const val API_DELETE_POST="post/{id}"
    const val API_DELETE_UNBLOCK_USER="blockUser/{id}"
    const val API_DELETE_UNLIKE="like/{id}"

    // update call
    const val API_UPDATE_USER="user"


    const val Profile_pic="https://i.pravatar.cc/150?img=3"
    const val Device_Token="1234567890"
    const val Timer_Value=60000



    const val SHARED_PREFS = "shared_prefs"
    const val PROFILE_DATA = "profileData"


}