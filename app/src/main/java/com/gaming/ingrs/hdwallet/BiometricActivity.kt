package com.gaming.ingrs.hdwallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.gaming.ingrs.hdwallet.backend.Cryptography
import com.gaming.ingrs.hdwallet.backend.Operations
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {

    companion object{
        lateinit var biometricTitle: TextView
        lateinit var biometricDescription: TextView
        lateinit var biometricWarningImage: ImageView
        lateinit var biometricButton: Button

        val PIN_RETURNED = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)
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

    fun biometricCheck(context: Context, fragment: FragmentActivity){
        val executor = ContextCompat.getMainExecutor(context)
        val biometricManager = androidx.biometric.BiometricManager.from(context)

        when (biometricManager.canAuthenticate()) {
            androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(executor, context, fragment)
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
            }
        }
    }

    private fun init(){
        biometricTitle = findViewById(R.id.biometricTitle)
        biometricDescription = findViewById(R.id.biometricDescription)
        biometricWarningImage = findViewById(R.id.biometricWarningImage)
        biometricButton = findViewById(R.id.biometricButton)

        biometricTitle.visibility = View.INVISIBLE
        biometricDescription.visibility = View.INVISIBLE
        biometricWarningImage.visibility = View.INVISIBLE
        biometricButton.visibility = View.INVISIBLE
    }

    private fun authUser(executor: Executor, context: Context, fragment: FragmentActivity) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication")
            //.setSubtitle("Owner confirmation")
            .setDescription("Scan fingerprint or enter PIN")
            .setNegativeButtonText("Use password")
            .build()

        val biometricPrompt = BiometricPrompt(fragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    //Error auth
                    if (errorCode == BiometricConstants.ERROR_USER_CANCELED) {
                        biometricTitle.visibility = View.VISIBLE
                        biometricDescription.visibility = View.VISIBLE
                        biometricWarningImage.visibility = View.VISIBLE
                        biometricButton.visibility = View.VISIBLE

                        biometricButton.text = context.getString(R.string.try_again)
                        biometricTitle.text = context.getString(R.string.biometric_user_canceled)
                        biometricDescription.text = context.getString(R.string.biometric_canceled_description)

                        biometricButton.setOnClickListener {
                            biometricCheck(context, fragment)
                        }
                    }
                    if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        val intent = Intent(context, PinActivity::class.java)
                        startActivityForResult(intent, PIN_RETURNED)
                    }
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    //Failed auth
                }
            })
        biometricPrompt.authenticate(promptInfo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            PIN_RETURNED -> {
                if(resultCode ==  Activity.RESULT_OK){
                    val encryptedTempPin = Operations().getHashMap("tempPin", this)
                    val secretKey = Cryptography().getSecretKey("tempPin")
                    val decryptPIN = Cryptography().decryptMsg(encryptedTempPin, secretKey)?.decodeToString()

                    //Here we will check if PIN is correct and move on
                }
            }
        }
    }

}
