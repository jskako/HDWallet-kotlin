package com.gaming.ingrs.hdwallet.fragments.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gaming.ingrs.hdwallet.MainActivity

import com.gaming.ingrs.hdwallet.R

class EndingWalletFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ending_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        next()
    }

    private fun next(){
        val runnable = Runnable {
            val intent = Intent(context, MainActivity::class.java)
            context?.startActivity(intent)
        }
        Handler().postDelayed(runnable, 2000)
    }

}
