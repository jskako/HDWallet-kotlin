package com.gaming.ingrs.hdwallet.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gaming.ingrs.hdwallet.MainActivity
import com.gaming.ingrs.hdwallet.backend.SafetyNetAttestation


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSafetyNet()
    }

    private fun newIntent(){
        //Call new intent after splash
        checkSafetyNet()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkSafetyNet() {
        val safetyNet= SafetyNetAttestation()
        safetyNet.sendSafetyNetRequest(this@SplashActivity, this)
    }

}