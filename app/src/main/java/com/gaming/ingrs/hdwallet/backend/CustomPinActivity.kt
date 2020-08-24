package com.gaming.ingrs.hdwallet.backend

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gaming.ingrs.hdwallet.R
import com.poovam.pinedittextfield.PinField.OnTextCompleteListener
import com.poovam.pinedittextfield.SquarePinField
import org.jetbrains.annotations.NotNull


class CustomPinActivity : AppCompatActivity() {

    companion object{
        const val TAG = "PIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_pin_activity)

        initPin()
    }

    private fun initPin(){
        val squarePinField = findViewById<SquarePinField>(R.id.enterPin)
        squarePinField.onTextCompleteListener = object : OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                Toast.makeText(this@CustomPinActivity, enteredText, Toast.LENGTH_SHORT).show()
                return true // Return false to keep the keyboard open else return true to close the keyboard
            }
        }
    }




}