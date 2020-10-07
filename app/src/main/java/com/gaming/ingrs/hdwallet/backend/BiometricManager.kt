package com.gaming.ingrs.hdwallet.backend

import android.content.Context
import android.content.Intent
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.gaming.ingrs.hdwallet.MainActivity
import com.gaming.ingrs.hdwallet.R
import java.util.concurrent.Executor
import kotlin.system.exitProcess

class BiometricManager {

    companion object{
        lateinit var biometricTitle: TextView
        lateinit var biometricDescription: TextView
        lateinit var biometricWarningImage: ImageView
        lateinit var biometricButton: Button
    }

    fun biometricCheck(context: Context, fragment: FragmentActivity){
        val executor = ContextCompat.getMainExecutor(context)
        val biometricManager = BiometricManager.from(context)
        init(fragment)

        when {
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS -> {
                authUser(executor, context, fragment)
            }
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                //No hardware
            }
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                //Hardware unvailable
            }
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                //NONE ENROLLED
            }
        }
    }

    private fun init(fragment: FragmentActivity){
        biometricTitle = fragment.findViewById(R.id.biometricTitle)
        biometricDescription = fragment.findViewById(R.id.biometricDescription)
        biometricWarningImage = fragment.findViewById(R.id.biometricWarningImage)
        biometricButton = fragment.findViewById(R.id.biometricButton)

        biometricTitle.visibility = INVISIBLE
        biometricDescription.visibility = INVISIBLE
        biometricWarningImage.visibility = INVISIBLE
        biometricButton.visibility = INVISIBLE
    }

    private fun authUser(executor: Executor, context: Context, fragment: FragmentActivity) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication")
            //.setSubtitle("Owner confirmation")
            .setDescription("Scan fingerprint or enter PIN")
            .setDeviceCredentialAllowed(true)
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
                        biometricTitle.visibility = VISIBLE
                        biometricDescription.visibility = VISIBLE
                        biometricWarningImage.visibility = VISIBLE
                        biometricButton.visibility = VISIBLE

                        biometricButton.text = context.getString(R.string.try_again)
                        biometricTitle.text = context.getString(R.string.biometric_user_canceled)
                        biometricDescription.text = context.getString(R.string.biometric_canceled_description)
                        biometricButton.setOnClickListener {
                            exitProcess(-1)
                        }
                    } else{

                    }
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    //Failed auth
                }
            })
        biometricPrompt.authenticate(promptInfo)
    }
}