package com.app.vibespace.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.app.vibespace.R
import com.app.vibespace.databinding.LayoutProgressBarBinding
import com.app.vibespace.service.Resources

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException


fun toast(view: View, string: String) {
    Toast.makeText(view.context,string, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, string: String) {
    Toast.makeText(context,string, Toast.LENGTH_SHORT).show()
}

fun handleApiError(exc: Exception, resources: android.content.res.Resources): Resources<Nothing> {
    when (exc) {
        is HttpException -> {
            exc.response()?.errorBody()?.let {
                return try {
                    val jsonData = JSONObject(it.string())
                    Resources.error(makeErrorBodyToString(jsonData), null)
                } catch (e: Exception) {
                    Resources.error(e.localizedMessage, null)
                }
            }
        }

        is UnknownHostException -> return Resources.error(
            resources.getString(R.string.checkInternet), null
        )

        else -> return Resources.error(
            resources.getString(R.string.tryAgain), null
        )
    }

    return Resources.error(resources.getString(R.string.tryAgain), null)
}

fun makeErrorBodyToString(jsonData: JSONObject): String {
    return if (jsonData.has("data")) {
        val getJson = jsonData.get("data")
        if (getJson is JSONObject) getJson.getString("message") else getJson.toString()
    } else {
        "Something went wrong. Please try again"
    }
}
fun Context.hideKeyboard(view: View) {
    val imm: InputMethodManager? =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}
