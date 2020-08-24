package com.gaming.ingrs.hdwallet.backend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gaming.ingrs.hdwallet.MainActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import com.google.android.gms.safetynet.SafetyNetApi.AttestationResponse
import com.google.android.gms.safetynet.SafetyNetClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.SecureRandom
import kotlin.system.exitProcess


class SafetyNetAttestation : AppCompatActivity() {

    companion object {
        val loadingSpinner = LoadingSpinner()
        private const val TAG = "SafetyNetSample"
        private val mRandom = SecureRandom()
        private var mResult: String? = null
        private const val API_KEY = "AIzaSyDSHoihxEGmqBxHsrb9WsmoRoh3w2xptbE"
        private lateinit var myContext: Context

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

    fun sendSafetyNetRequest(activity: Activity, context: Context) {
        myContext = context
        //Show Loading Spinner
        loadingSpinner.startLoadingSpinner(
            myContext,
            "Verifying SafetyNet Attestation",
            "Checking requirements"
        )
        val nonceData = "Safety Net Sample: " + System.currentTimeMillis()
        val nonce: ByteArray = getRequestNonce(nonceData)
        //activity needs to be fixed
        val client: SafetyNetClient = SafetyNet.getClient(activity)
        val task: Task<SafetyNetApi.AttestationResponse> = client.attest(nonce, API_KEY)
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
            /*
                             TODO(developer): Forward this result to your server together with
                             the nonce for verification.
                             You can also parse the JwsResult locally to confirm that the API
                             returned a response by checking for an 'error' field first and before
                             retrying the request with an exponential backoff.
                             NOTE: Do NOT rely on a local, client-side only check for security, you
                             must verify the response on a remote server!
                            */
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

        checkIfPINExists()

        loadingSpinner.stopTimer()
        val intent = Intent(myContext, MainActivity::class.java)
        //val intent = Intent(myContext, CustomPinActivity::class.java)
        myContext.startActivity(intent)
    }

    private fun checkIfPINExists(){
        // Settings Lock Startup Enabled
        // Does PIN exists - create / enter
    }

}