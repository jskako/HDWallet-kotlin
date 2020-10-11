package com.gaming.ingrs.hdwallet.fragments.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.gaming.ingrs.hdwallet.R

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    lateinit var next: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        next = requireActivity().findViewById(R.id.welcomeButton) as Button
        when(checkSteps()){
            1 -> {
                next.setOnClickListener {
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.welcome_container, PinFragment())
                        .addToBackStack(null)
                        .commit()
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

}
