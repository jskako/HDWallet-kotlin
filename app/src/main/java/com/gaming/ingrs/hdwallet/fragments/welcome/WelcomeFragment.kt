package com.gaming.ingrs.hdwallet.fragments.welcome

import android.app.Activity
import android.content.Intent
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
import com.gaming.ingrs.hdwallet.backend.hideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    lateinit var next: Button
    lateinit var description: TextView

    companion object{
        const val PIN_RETURNED = 1
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
                    hideKeyboard()
                    val intent = Intent(context, PinActivity::class.java)
                    intent.putExtra("description", getString(R.string.pin_identification_description))
                    intent.putExtra("keySecret", "pin")
                    intent.putExtra("returnActivityResult", "1")
                    startActivityForResult(intent, PIN_RETURNED)
                }
            }
            2 -> {
                next.setOnClickListener {
                    hideKeyboard()
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.welcome_container, MailFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            3 -> {
                next.setOnClickListener {
                    hideKeyboard()
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.welcome_container, WalletFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    private fun checkSteps():Int{
        // 1 - PIN Needed, 2 - Mail Needed, 3 - Wallet Needed
        var checker: Boolean = Operations().checkPinExists(requireActivity())
        if(!checker){
            return 1
        }

        checker = Operations().checkMailExists(requireActivity())
        if(!checker){
            return 2
        }

        checker = Operations().checkWalletExists(requireActivity())
        if(!checker){
            return 3
        }

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

                        val pinCorrect = activity?.let { context?.let { it1 ->
                            Cryptography().pinCompare(it,
                                it1
                            )
                        } }
                        if(pinCorrect!!){
                            Operations().deleteFromSharedPreferences("tempPin", requireContext())
                            requireFragmentManager().beginTransaction()
                                .replace(R.id.welcome_container, MailFragment())
                                .addToBackStack(null)
                                .commit()
                        }else{
                            Operations().deleteFromSharedPreferences("pin", requireContext())
                            Operations().deleteFromSharedPreferences("tempPin", requireContext())
                            description.text = getString(R.string.incorrect_pin_check)
                            description.setTextColor(resources.getColor(R.color.red))
                        }
                    }
                }
            }
        }
    }
}
