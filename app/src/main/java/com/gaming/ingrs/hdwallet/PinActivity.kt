package com.gaming.ingrs.hdwallet

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gaming.ingrs.hdwallet.backend.Cryptography
import com.gaming.ingrs.hdwallet.backend.Operations

class PinActivity() : AppCompatActivity() {

    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)

    companion object {

        const val NUM_OF_DIGITS = 4
        var numTemp = ""
        lateinit var description: TextView
        lateinit var secretKeyLocation: String
        lateinit var returnActivityResult: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)
        changeActionBar(R.color.welcome_background)
        init()
        val layout: LinearLayout = findViewById(R.id.codeLayout)
        for (index in 0 until (layout.childCount)) {
            val view: View = layout.getChildAt(index)
            if (view is EditText) {
                editTextArray.add(index, view)
                editTextArray[index].addTextChangedListener(textWatcher)
            }
        }

        editTextArray[0].requestFocus()
    }

    private fun init(){
        description = findViewById(R.id.pinDescription)
        val descriptionText = intent.getStringExtra("description")!!
        secretKeyLocation = intent.getStringExtra("keySecret")!!
        returnActivityResult = intent.getStringExtra("returnActivityResult")

        description.text = descriptionText
    }

    private fun changeActionBar(resourceColor: Int){
        window.statusBarColor = ContextCompat.getColor(applicationContext, resourceColor)
    }

    private fun testCodeValidity(): String {
        var verificationCode = ""
        for (i in editTextArray.indices) {
            val digit = editTextArray[i].text.toString().trim { it <= ' ' }
            verificationCode += digit
        }
        if (verificationCode.trim { it <= ' ' }.length == NUM_OF_DIGITS) {
            return verificationCode
        }
        return ""
    }

    private fun verifyCode(verificationCode: String) {
        if (verificationCode.isNotEmpty()) {
            enableCodeEditTexts(false)
            //Your code
        }
    }

    private fun enableCodeEditTexts(enable: Boolean) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].isEnabled = enable
    }

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {

            (0 until editTextArray.size)
                .forEach { i ->
                    if (s === editTextArray[i].editableText) {
                        if (s!!.isBlank()) {
                            return
                        }
                        if (s.length >= 2) {//if more than 1 char
                            val newTemp = s.toString().substring(s.length - 1, s.length)
                            if (newTemp != numTemp) {
                                editTextArray[i].setText(newTemp)
                            } else {
                                editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                            }
                        } else if (i != editTextArray.size - 1) { //not last char
                            editTextArray[i + 1].requestFocus()
                            editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                            return
                        } else {
                            verifyCode(testCodeValidity())
                            val digOne: EditText = findViewById(R.id.digit1)
                            val digTwo: EditText = findViewById(R.id.digit2)
                            val digThree: EditText = findViewById(R.id.digit3)
                            val digFour: EditText = findViewById(R.id.digit4)

                            val pin = digOne.text.toString() + digTwo.text.toString() + digThree.text.toString() + digFour.text.toString()
                            val secretKey = Cryptography().generateSecretKey(secretKeyLocation)
                            val encryptedTempPin = Cryptography().encryptMsg(pin, secretKey)
                            Operations().saveHashMap(secretKeyLocation, encryptedTempPin, this@PinActivity)

                            when(returnActivityResult){
                                "1" -> setResult(Activity.RESULT_OK)
                                "2" -> setResult(Activity.RESULT_FIRST_USER)
                            }
                            finish()
                        }
                    }
                }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            numTemp = s.toString()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}

