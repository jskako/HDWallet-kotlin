package com.gaming.ingrs.hdwallet.backend

class AutomateBoringStuff {
    fun convertListToString(
        words: List<String>,
        delimiter: String
    ): String {
        val builder = StringBuilder()
        for (details in words) {
            //Log.e("Details: ",details);
            builder.append(details + delimiter)
        }
        return builder.toString()
    }
}