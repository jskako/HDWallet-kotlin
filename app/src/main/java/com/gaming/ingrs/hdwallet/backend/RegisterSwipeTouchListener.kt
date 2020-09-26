package com.gaming.ingrs.hdwallet.backend

import android.annotation.SuppressLint
import android.content.Context
import android.widget.FrameLayout
import android.widget.Toast

class RegisterSwipeTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchListener(context: Context, layout: FrameLayout){
        layout.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                Toast.makeText(
                    context,
                    "Swipe Right gesture detected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}