package com.gaming.ingrs.hdwallet.backend

import android.util.Log
import org.bitcoinj.crypto.*
import java.security.SecureRandom

class BitcoinAPI {
    private val BIP44PATH = "44H/0H/0H"

    //Entropy of 128Bits
    fun entropyGenerator(): ByteArray {
        val entropyLen = 16
        val entropy = ByteArray(entropyLen)
        val random = SecureRandom()
        random.nextBytes(entropy)
        return entropy
    }

    //Mnemonoic generator
    fun mnemonicGenerator(entropy: ByteArray = entropyGenerator()): List<String> {
        lateinit var words: List<String>
        try {
            words = MnemonicCode.INSTANCE.toMnemonic(entropy)
        } catch (e: Exception) {
            Log.e("Error", "Couldn't generate mnemonic")
        }
        return words
    }

    //Seed generator
    fun seedGenerator(words: List<String>, passphrase: String): ByteArray {
        return MnemonicCode.toSeed(words, passphrase)
    }

    //Generate master key
    fun generateMasterKey(seed: ByteArray): DeterministicKey {
        return HDKeyDerivation.createMasterPrivateKey(seed)
    }

    //Derive bitcoin root key
    fun deriveBitcoinRootKey(masterKey: DeterministicKey): DeterministicKey {
        val keyPath: List<ChildNumber> = HDUtils.parsePath(BIP44PATH)
        val hierarchy = DeterministicHierarchy(masterKey)
        return hierarchy.get(keyPath, false, true)
    }
}