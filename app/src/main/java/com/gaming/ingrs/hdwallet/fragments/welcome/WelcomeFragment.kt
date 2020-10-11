package com.gaming.ingrs.hdwallet.fragments.welcome

import android.app.Activity
import android.content.Intent
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.gaming.ingrs.hdwallet.PinActivity

import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.Cryptography
import com.gaming.ingrs.hdwallet.backend.Operations

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    companion object{
        val PIN_RETURNED = 1
        lateinit var next: Button
        lateinit var description: TextView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        description = requireActivity().findViewById(R.id.description)
        next = requireActivity().findViewById(R.id.welcomeButton) as Button
        when(checkSteps()){
            1 -> {
                next.setOnClickListener {
                    val intent = Intent(context, PinActivity::class.java)
                    intent.putExtra("description", getString(R.string.pin_identification_description))
                    intent.putExtra("keySecret", "pin")
                    intent.putExtra("returnActivityResult", "1")
                    startActivityForResult(intent, PIN_RETURNED)
                }
            }
            2 -> {
                next.setOnClickListener {
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.welcome_container, MailFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            3 -> {
                next.setOnClickListener {
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.welcome_container, WalletFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    private fun checkSteps():Int{
        //We will check PIN, wallet, and mail
        // 1 - PIN Needed, 2 - Mail Needed, 3 - Wallet Needed
        return 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            PIN_RETURNED -> {
                when(resultCode){
                    Activity.RESULT_OK -> {
                        val intent = Intent(context, PinActivity::class.java)
                        intent.putExtra("description", "Repeat Your PIN")
                        intent.putExtra("keySecret", "tempPin")
                        intent.putExtra("returnActivityResult", "2")
                        startActivityForResult(intent, PIN_RETURNED)
                    }
                    Activity.RESULT_FIRST_USER -> {
                        val encryptedPin =
                            activity?.let { Operations().getHashMap("pin", it) }
                        val encryptedTempPin =
                            activity?.let { Operations().getHashMap("tempPin", it) }
                        val secretKeyPin = Cryptography().getSecretKey("pin")
                        val secretKeyTempPin = Cryptography().getSecretKey("tempPin")
                        val decryptPIN = Cryptography().decryptMsg(encryptedPin, secretKeyPin)?.decodeToString()
                        val decryptTempPIN = Cryptography().decryptMsg(encryptedTempPin, secretKeyTempPin)?.decodeToString()
                        if(decryptPIN == decryptTempPIN){
                            Log.e("123","PIN CORRECT")
                            requireFragmentManager().beginTransaction()
                                .replace(R.id.welcome_container, MailFragment())
                                .addToBackStack(null)
                                .commit()
                        }else{
                            description.text = getString(R.string.incorrect_pin)
                            description.setTextColor(resources.getColor(R.color.red))
                            Log.e("123","PIN INCORRECT")
                        }
                    }
                }
            }
        }
    }
}
