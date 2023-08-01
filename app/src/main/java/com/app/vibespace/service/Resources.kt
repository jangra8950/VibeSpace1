package com.app.vibespace.service

import com.app.vibespace.Enums.ApiStatus

data class Resources<out T>(
    val status: ApiStatus,
    val data: T?,
    val message: String?
) {
    companion object {

        fun <T> success(data: T?): Resources<T> {
            return Resources(ApiStatus.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resources<T> {
            return Resources(ApiStatus.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resources<T> {
            return Resources(ApiStatus.LOADING, data, null)
        }
    }
}