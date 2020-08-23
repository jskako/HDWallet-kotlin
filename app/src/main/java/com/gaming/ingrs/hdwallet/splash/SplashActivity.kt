package com.gaming.ingrs.hdwallet.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gaming.ingrs.hdwallet.backend.SafetyNetAttestation


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSafetyNet()
    }

    private fun checkSafetyNet() {
        val safetyNet= SafetyNetAttestation()
        safetyNet.sendSafetyNetRequest(this@SplashActivity, this)
    }

}