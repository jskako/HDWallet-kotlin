package com.gaming.ingrs.hdwallet.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gaming.ingrs.hdwallet.MainActivity


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newIntent()
    }

    fun newIntent(){
        //Call new intent after splash
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}