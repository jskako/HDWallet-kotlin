package com.gaming.ingrs.hdwallet.backend

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import kotlin.system.exitProcess


class LoadingSpinner {

    companion object{
        val handler = Handler()
        lateinit var nDialog: ProgressDialog
    }

    fun startLoadingSpinner(context: Context, message: String, title: String, exitTimer: Long = 60){
        nDialog = ProgressDialog(context)
        nDialog.setMessage(message)
        nDialog.setTitle(title)
        nDialog.isIndeterminate = false
        nDialog.setCancelable(false)
        nDialog.show()
        exitTimer(exitTimer)
    }

    fun stopLoadingSpinner(context: Context){
        nDialog = ProgressDialog(context)
        nDialog.hide()
    }

    //If stuck on LoadingSpinner for 60 seconds exit app
    private fun exitTimer(exitTimer: Long){
        handler.postDelayed(Runnable {
            exitProcess(-1)
        }, 1000 * exitTimer) //after amount of exitTimer seconds

    }

    fun stopTimer(){
        handler.removeCallbacksAndMessages(null);
    }

}