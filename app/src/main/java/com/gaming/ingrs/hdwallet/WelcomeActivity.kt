package com.gaming.ingrs.hdwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gaming.ingrs.hdwallet.fragments.welcome.WelcomeFragment

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        hideToolbar()
        addFragment()
    }

    private fun hideToolbar(){
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun addFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.welcome_container, WelcomeFragment())
            .commitAllowingStateLoss()
    }
}
