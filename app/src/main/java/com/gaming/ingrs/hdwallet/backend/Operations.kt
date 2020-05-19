package com.gaming.ingrs.hdwallet.backend

import android.app.Activity
import android.content.Context

class Operations {

    fun convertListToString(
        words: List<String>,
        delimiter: String
    ): String {
        val builder = StringBuilder()
        for (details in words) {
            //Log.e("Details: ",details);
            builder.append(details)
            builder.append(delimiter)
        }
        return builder.toString()
    }

    fun writeToSharedPreferences(activity: Activity, key: String, data: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, data)
            commit()
        }
    }

    fun readFromSharedPreferences(activity: Activity, key: String): String? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val highScore = sharedPref.getString(key, "0")
        return highScore
    }

}