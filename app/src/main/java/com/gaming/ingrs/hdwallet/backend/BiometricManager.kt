package com.gaming.ingrs.hdwallet.backend

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.concurrent.Executor

class BiometricManager {

    fun biometricCheck(context: Context, fragment: Fragment){

        val executor = ContextCompat.getMainExecutor(context)
        val biometricManager = BiometricManager.from(context)

        when {
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS -> {
                authUser(executor, context, fragment)
            }
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    context,
                    ("No hardware"),
                    Toast.LENGTH_LONG
                ).show()
            }
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    context,
                    //getString(R.string.error_msg_biometric_hw_unavailable)
                    "Hardware unvailable",
                    Toast.LENGTH_LONG
                ).show()
            }
            biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    context,
                    "NONE ENROLLED",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun authUser(executor: Executor, context: Context, fragment: Fragment) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setSubtitle("Subtitle")
            .setDescription("Description")
            .setDeviceCredentialAllowed(true)
            .build()

        val biometricPrompt = BiometricPrompt(fragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        context,
                        "I did it",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        context,
                        "Error auth",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context,
                        "Failed auth",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}