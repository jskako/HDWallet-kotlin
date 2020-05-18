package com.gaming.ingrs.hdwallet.backend

import android.os.AsyncTask

class DoAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}