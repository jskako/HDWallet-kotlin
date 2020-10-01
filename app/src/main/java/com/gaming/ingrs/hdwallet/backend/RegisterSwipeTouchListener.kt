package com.gaming.ingrs.hdwallet.backend

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.gaming.ingrs.hdwallet.MainActivity
import com.gaming.ingrs.hdwallet.R


class RegisterSwipeTouchListener: AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchListener(context: Context, layout: FrameLayout, view: View){
        layout.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                //Open navigation menu

            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                //Back to previous fragment
                view.findNavController().navigateUp()
            }
        })
    }
}
