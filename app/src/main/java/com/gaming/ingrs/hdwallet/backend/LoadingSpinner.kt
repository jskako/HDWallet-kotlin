package com.gaming.ingrs.hdwallet.backend

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.core.content.ContextCompat.startActivity
import kotlin.system.exitProcess


class LoadingSpinner {

    companion object{
        val handler = Handler()
    }

    fun startLoadingSpinner(context: Context, message: String, title: String, exitTimer: Long = 60){
        val nDialog = ProgressDialog(context)
        nDialog.setMessage(message)
        nDialog.setTitle(title)
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)
        nDialog.show()
        exitTimer(exitTimer)
    }

    //If stuck on LoadingSpinner for 60 seconds exit app
    private fun exitTimer(exitTimer: Long){
        handler.postDelayed(Runnable {
            // yourMethod();
            exitProcess(-1)
        }, 1000 * exitTimer) //after amount of exitTimer seconds

    }

    fun stopTimer(){
        handler.removeCallbacksAndMessages(null);
    }

}