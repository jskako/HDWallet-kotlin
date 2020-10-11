package com.gaming.ingrs.hdwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.gaming.ingrs.hdwallet.fragments.welcome.WelcomeFragment

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        changeActionBar(R.color.welcome_background)
        addFragment()
    }

    override fun onBackPressed() {
        // Disable back button, do nothing
    }

    private fun addFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.welcome_container, WelcomeFragment())
            .commitAllowingStateLoss()
    }

    private fun changeActionBar(resourceColor: Int){
        window.statusBarColor = ContextCompat.getColor(applicationContext, resourceColor)
    }

}
