package com.gaming.ingrs.hdwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.gaming.ingrs.hdwallet.backend.BiometricManager

class BiometricActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.welcome_background)
        biometricCheck()
    }

    private fun biometricCheck(){
        BiometricManager().biometricCheck(this, this)
    }
}
