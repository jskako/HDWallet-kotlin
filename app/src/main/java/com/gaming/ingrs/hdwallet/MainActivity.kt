package com.gaming.ingrs.hdwallet

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gaming.ingrs.hdwallet.backend.AutomateBoringStuff
import com.gaming.ingrs.hdwallet.backend.BitcoinAPI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()
    }

    private fun test() {
        val textView = findViewById<TextView>(R.id.test)
        val bc = BitcoinAPI()
        val entropy: ByteArray = bc.entropyGenerator()
        val words: List<String> = bc.mnemonicGenerator(entropy)
        val abs = AutomateBoringStuff()
        val myWords: String = abs.convertListToString(words, " ")
        textView.text = myWords
    }
}
