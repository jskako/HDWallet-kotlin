package com.gaming.ingrs.hdwallet.backend

import android.content.Context
import android.content.Intent
import android.util.Log
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
import android.provider.Settings
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE

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

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(executor, context, fragment)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
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
                        biometricTitle.visibility = VISIBLE
                        biometricDescription.visibility = VISIBLE
                        biometricWarningImage.visibility = VISIBLE
                        biometricButton.visibility = VISIBLE

                        biometricButton.text = context.getString(R.string.try_again)
                        biometricTitle.text = context.getString(R.string.biometric_user_canceled)
                        biometricDescription.text = context.getString(R.string.biometric_canceled_description)

                        biometricButton.setOnClickListener {
                            BiometricManager().biometricCheck(context, fragment)
                        }
                    }
                    if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        Log.e("User clicked on negative button", "User clicked")
                        //loginWithPassword() // Because negative button says use application password
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