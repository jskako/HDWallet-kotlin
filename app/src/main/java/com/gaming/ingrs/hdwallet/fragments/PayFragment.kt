package com.gaming.ingrs.hdwallet.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.gaming.ingrs.hdwallet.R
import com.google.zxing.integration.android.IntentIntegrator

/**
 * A simple [Fragment] subclass.
 */
class PayFragment : Fragment() {

    companion object{
        lateinit var button : Button
        lateinit var text : TextView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        // Test QR Scanner
        button = requireView().findViewById(R.id.button) as Button
        text = requireView().findViewById(R.id.textView) as TextView

        button.setOnClickListener {
            scanQRCode()
        }
    }

    private fun scanQRCode() {
        val intentIntegrator = IntentIntegrator(activity)
        intentIntegrator.setBeepEnabled(false)
        intentIntegrator.setCameraId(0)
        intentIntegrator.setPrompt("SCAN")
        intentIntegrator.setBarcodeImageEnabled(false)
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MainActivity", "Scanned")
                Toast.makeText(context, "Scanned -> " + result.contents, Toast.LENGTH_SHORT)
                    .show()
                text.text = result.contents
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
