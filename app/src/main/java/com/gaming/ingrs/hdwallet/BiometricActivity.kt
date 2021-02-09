package com.gaming.ingrs.hdwallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.gaming.ingrs.hdwallet.backend.Cryptography
import com.gaming.ingrs.hdwallet.backend.hideKeyboard
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {

    lateinit var biometricTitle: TextView
    lateinit var biometricDescription: TextView
    lateinit var biometricWarningImage: ImageView
    lateinit var biometricButton: Button

    companion object{
        const val PIN_RETURNED = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)
        //Set this to gray
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.welcome_background)
        init()
        biometricCheck()
    }

    private fun biometricCheck(){
        biometricCheck(this, this)
    }

    override fun onBackPressed() {
        // Disable back button, do nothing
    }

    private fun biometricCheck(context: Context, fragment: FragmentActivity){
        val executor = ContextCompat.getMainExecutor(context)
        val biometricManager = androidx.biometric.BiometricManager.from(context)

        when (biometricManager.canAuthenticate()) {
            androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(executor, context, fragment)
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                negativeButtonCall()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                negativeButtonCall()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                negativeButtonCall()
            }
        }
    }

    private fun init() {
        changeActionBar(R.color.biometric_background)

        biometricTitle = findViewById(R.id.biometricTitle)
        biometricDescription = findViewById(R.id.biometricDescription)
        biometricWarningImage = findViewById(R.id.biometricWarningImage)
        biometricButton = findViewById(R.id.biometricButton)
        hideEverything()
    }


    private fun hideEverything(){
        biometricTitle.visibility = View.INVISIBLE
        biometricDescription.visibility = View.INVISIBLE
        biometricWarningImage.visibility = View.INVISIBLE
        biometricButton.visibility = View.INVISIBLE
    }

    private fun authUser(executor: Executor, context: Context, fragment: FragmentActivity) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication")
            .setDescription("Scan fingerprint or enter PIN")
            .setNegativeButtonText("Use password")
            .build()

        val biometricPrompt = BiometricPrompt(fragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    hideKeyboard()
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    hideKeyboard()
                    when(errorCode) {
                        BiometricConstants.ERROR_USER_CANCELED -> {
                            onError(getString(R.string.biometric_user_canceled), getString(R.string.biometric_canceled_description))
                        }
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_CANCELED -> {
                            onError(getString(R.string.auth_failed), getString(R.string.pin_identification_description))
                        }
                        BiometricConstants.ERROR_HW_NOT_PRESENT -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_HW_UNAVAILABLE -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_LOCKOUT -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_LOCKOUT_PERMANENT -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_NO_BIOMETRICS -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_NO_DEVICE_CREDENTIAL -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_NO_SPACE -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_TIMEOUT -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_UNABLE_TO_PROCESS -> {
                            negativeButtonCall()
                        }
                        BiometricConstants.ERROR_VENDOR -> {
                            negativeButtonCall()
                        }
                    }
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    hideKeyboard()
                    //Failed auth
                    onError(getString(R.string.auth_failed), getString(R.string.pin_identification_description))
                }
            })
        biometricPrompt.authenticate(promptInfo)
    }

    fun negativeButtonCall(){
        val intent = Intent(this, PinActivity::class.java)
        intent.putExtra("description", getString(R.string.pin_identification_description))
        intent.putExtra("returnActivityResult", "1")
        intent.putExtra("keySecret", "tempPin")
        startActivityForResult(intent, PIN_RETURNED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            PIN_RETURNED -> {
                if(resultCode ==  Activity.RESULT_OK){
                    successGoNext()
                }
            }
        }
    }

    private fun successGoNext(){
        val pinCorrect = Cryptography().pinCompare(this, this)
        if(pinCorrect){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
            onError(getString(R.string.incorrect_pin), getString(R.string.pin_identification_description))
        }
    }

    private fun onError(title: String, description: String){
        changeActionBar(R.color.welcome_background)
        biometricTitle.visibility = View.VISIBLE
        biometricDescription.visibility = View.VISIBLE
        biometricWarningImage.visibility = View.VISIBLE
        biometricButton.visibility = View.VISIBLE

        biometricButton.text = getString(R.string.try_again)
        biometricTitle.text = title
        biometricDescription.text = description

        biometricButton.setOnClickListener {
            hideEverything()
            changeActionBar(R.color.biometric_background)
            biometricCheck(this, this)
        }
    }

    private fun changeActionBar(resourceColor: Int){
        window.statusBarColor = ContextCompat.getColor(applicationContext, resourceColor)
    }

}
