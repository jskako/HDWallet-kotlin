package com.gaming.ingrs.hdwallet.backend

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
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
import java.text.ParseException


class SafetynetAttestation: Fragment() {

    private val TAG = "SafetyNetSample"
    private val mRandom = SecureRandom()
    private var mResult: String? = null
    private val API_KEY = "AIzaSyDSHoihxEGmqBxHsrb9WsmoRoh3w2xptbE"

    fun sendSafetyNetRequest(activity: Activity) {
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
        }

}