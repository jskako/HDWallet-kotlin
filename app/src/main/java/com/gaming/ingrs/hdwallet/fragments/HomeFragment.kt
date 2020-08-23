package com.gaming.ingrs.hdwallet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.QRCodeGenerator

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        testQR()
    }

    private fun testQR() {
        val button = requireView().findViewById(R.id.qrGenerator) as Button
        val imv = requireView().findViewById(R.id.imageViewQR) as ImageView
        val text = requireView().findViewById(R.id.text) as TextView

        // Test QR Generator
        button.setOnClickListener {
            Toast.makeText(context, "message", Toast.LENGTH_SHORT).show()
            val qrc = QRCodeGenerator()
            val bitmap = qrc.generateQRCode("Something Generated")
            imv.setImageBitmap(bitmap)
        }
    }
}
