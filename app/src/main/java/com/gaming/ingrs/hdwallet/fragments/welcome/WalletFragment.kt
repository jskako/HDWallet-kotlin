package com.gaming.ingrs.hdwallet.fragments.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast

import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.hideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class WalletFragment : Fragment() {

    companion object{
        lateinit var newWalletButton: Button
        lateinit var recoverWalletButton: Button
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        setButtons()
    }

    private fun setButtons(){
        newWalletButton.setOnClickListener {
            hideKeyboard()
            requireFragmentManager().beginTransaction()
                .replace(R.id.welcome_container, NewWalletFragment())
                .addToBackStack(null)
                .commit()
        }

        recoverWalletButton.setOnClickListener {
            hideKeyboard()
            Toast.makeText(
                context,
                "Not implemented yet!",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private fun init(){
        newWalletButton = requireActivity().findViewById(R.id.newWalletButton)
        recoverWalletButton = requireActivity().findViewById(R.id.recoverWalletButton)
    }

}
