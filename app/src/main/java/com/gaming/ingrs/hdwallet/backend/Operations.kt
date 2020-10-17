package com.gaming.ingrs.hdwallet.backend

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.regex.Matcher
import java.util.regex.Pattern


class Operations {

    companion object{
        const val PIN_LOC = "pin"
        const val SEED_LOC = "seedPhrase"
        const val MAIL_LOC = "userMail"
    }

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

    fun writeToSharedPreferences(activity: Activity, key: String, data: String) {
        val sharedPreference =  activity.getSharedPreferences("HDWallet",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(key,data)
        editor.commit()
    }

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun readFromSharedPreferences(activity: Activity, key: String): String? {
        val sharedPreference =  activity.getSharedPreferences("HDWallet",Context.MODE_PRIVATE)
        return sharedPreference.getString(key,"0")
    }

    fun deleteFromSharedPreferences(key: String, context: Context){
        val mySPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = mySPrefs.edit()
        editor.remove(key)
        editor.apply()
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

    fun checkPinExists(activity: Activity): Boolean{
        val encryptedPin = Operations().getHashMap(PIN_LOC, activity)
        return encryptedPin != null
    }

    fun checkMailExists(activity: Activity): Boolean{
        val profileMail = Operations().readFromSharedPreferences(activity, MAIL_LOC)
        return profileMail != "0"
    }

    fun checkWalletExists(activity: Activity): Boolean{
        val walletCheck = Operations().getHashMap(SEED_LOC, activity)
        return walletCheck != null
    }

}