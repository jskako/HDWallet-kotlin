package com.gaming.ingrs.hdwallet.backend

import android.app.Activity
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class Cryptography {

    fun generateSecretKey(secretKeyAlias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        val spec = KeyGenParameterSpec
            .Builder(secretKeyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    fun getSecretKey(secretKeyAlias: String): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKeyEntry = keyStore.getEntry(secretKeyAlias, null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    fun encryptMsg(message: String, secret: SecretKey?): HashMap<String, ByteArray>? {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(message.toByteArray())
        val map =  HashMap<String, ByteArray>()
        map["iv"] = ivBytes
        map["encrypted"] = encryptedBytes
        return map
    }

    fun decryptMsg(myHashedMap: HashMap<String, ByteArray>?, secret: SecretKey?): ByteArray? {
        val encryptedBytes = myHashedMap?.get("encrypted")
        val ivBytes = myHashedMap?.get("iv")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, secret, spec)
        return cipher.doFinal(encryptedBytes)
    }

    fun pinCompare(activity: Activity, context: Context): Boolean{
        val encryptedTempPin = Operations().getHashMap("tempPin", activity)
        val secretTempPinKey = Cryptography().getSecretKey("tempPin")
        val decryptTempPIN = Cryptography().decryptMsg(encryptedTempPin, secretTempPinKey)?.decodeToString()

        val encryptedPin = Operations().getHashMap("pin", activity)
        val secretPINKey = Cryptography().getSecretKey("pin")
        val decryptPIN = Cryptography().decryptMsg(encryptedPin, secretPINKey)?.decodeToString()

        Operations().deleteFromSharedPreferences("tempPin", context)

        return decryptTempPIN == decryptPIN
    }

    fun pinExists(activity: Activity): Boolean{
        val encryptedTempPin = Operations().getHashMap("pin", activity)
        val secretTempPinKey = Cryptography().getSecretKey("pin")
        val decryptTempPIN = Cryptography().decryptMsg(encryptedTempPin, secretTempPinKey)?.decodeToString()
        return !decryptTempPIN.isNullOrEmpty()
    }
}