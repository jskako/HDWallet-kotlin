package com.gaming.ingrs.hdwallet.backend

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Operations {

    fun convertListToString(
        words: List<String>,
        delimiter: String
    ): String {
        val builder = StringBuilder()
        for (details in words) {
            builder.append(details)
            builder.append(delimiter)
        }
        return builder.toString()
    }

    private fun writeToSharedPreferences(activity: Activity, key: String, data: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, data)
            commit()
        }
    }

    fun readFromSharedPreferences(activity: Activity, key: String): String? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, "0")
    }

    fun saveHashMap(key: String?, obj: Any?, activity: Activity) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = prefs.edit()
        val gson = Gson()
        val json: String = gson.toJson(obj)
        editor.putString(key, json)
        editor.apply()
    }

    fun getHashMap(key: String?, activity: Activity): HashMap<String, ByteArray>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val gson = Gson()
        val json = prefs.getString(key, "")
        val type: Type = object : TypeToken<HashMap<String?, ByteArray>?>() {}.type
        return gson.fromJson(json, type)
    }

}