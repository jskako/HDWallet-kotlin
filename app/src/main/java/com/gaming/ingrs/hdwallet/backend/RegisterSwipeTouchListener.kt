package com.gaming.ingrs.hdwallet.backend

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController


class RegisterSwipeTouchListener: AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchListener(context: Context, layout: FrameLayout, view: View){
        layout.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                view.findNavController().navigateUp()
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                //Back to previous fragment
            }
        })
    }
}
