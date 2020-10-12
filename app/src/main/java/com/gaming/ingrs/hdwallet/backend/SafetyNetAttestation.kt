package com.gaming.ingrs.hdwallet.backend

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gaming.ingrs.hdwallet.BiometricActivity
import com.gaming.ingrs.hdwallet.WelcomeActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi.AttestationResponse
import com.google.android.gms.safetynet.SafetyNetClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.SecureRandom
import kotlin.system.exitProcess


@SuppressLint("Registered")
class SafetyNetAttestation : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var activity: Activity

    companion object {
        val loadingSpinner = LoadingSpinner()
        private const val TAG = "SafetyNetSample"
        private val mRandom = SecureRandom()
        private var mResult: String? = null
        private const val API_KEY = "AIzaSyDSHoihxEGmqBxHsrb9WsmoRoh3w2xptbE"

        var thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(Toast.LENGTH_LONG.toLong())
                    exitProcess(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun sendSafetyNetRequest(myActivity: Activity, myContext: Context) {
        this.context = myContext
        this.activity = myActivity
        //Show Loading Spinner
        loadingSpinner.startLoadingSpinner(
            context,
            "Verifying SafetyNet Attestation",
            "Checking requirements"
        )
        val nonceData = "Safety Net Sample: " + System.currentTimeMillis()
        val nonce: ByteArray = getRequestNonce(nonceData)
        //activity needs to be fixed
        val client: SafetyNetClient = SafetyNet.getClient(activity)
        val task: Task<AttestationResponse> = client.attest(nonce, API_KEY)
        task.addOnSuccessListener(activity, mSuccessListener)
            .addOnFailureListener(activity, mFailureListener)
    }

    private fun getRequestNonce(data: String): ByteArray {
        val byteStream = ByteArrayOutputStream()
        val bytes = ByteArray(24)
        mRandom.nextBytes(bytes)
        try {
            byteStream.write(bytes)
            byteStream.write(data.toByteArray())
        } catch (e: IOException) {
            return "".toByteArray()
        }
        return byteStream.toByteArray()
    }

    private val mSuccessListener =
        OnSuccessListener<AttestationResponse> { attestationResponse ->
            mResult = attestationResponse.jwsResult
            Log.d(TAG, "Success! SafetyNet result:\n$mResult\n")
            successNext()
        }

    private val mFailureListener: OnFailureListener =
        OnFailureListener { e ->
            mResult = null
            if (e is ApiException) {
                val apiException: ApiException = e as ApiException
                Log.d(
                    TAG, "Error: " +
                            CommonStatusCodes.getStatusCodeString(apiException.getStatusCode())
                                .toString() + ": " +
                            apiException.getStatusMessage()
                )
            } else {
                Log.d(TAG, "ERROR! " + e.message)
            }
            Toast.makeText(
                this,
                "SafetyNetAttestation Failed! Your device might be rooted?",
                Toast.LENGTH_LONG
            ).show();
            thread.start();
        }

    private fun successNext() {
        loadingSpinner.stopTimer()
        when(checkIfWalletExist()){
            false -> {
                val intent = Intent(context, WelcomeActivity::class.java)
                context.startActivity(intent)
            }
            true -> {
                val intent = Intent(context, BiometricActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    private fun checkIfWalletExist(): Boolean{
        //TODO: Check profile, wallet
        var check = false
        val operations = Operations()

        //Check PIN
        check = Cryptography().pinExists(activity)

        //Check profile
        val profileMail = operations.readFromSharedPreferences(activity, "profileMail")
        check = !profileMail.equals("0")

        return check
    }
}