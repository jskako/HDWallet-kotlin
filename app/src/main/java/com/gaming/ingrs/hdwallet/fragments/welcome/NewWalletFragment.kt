package com.gaming.ingrs.hdwallet.fragments.welcome

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.gaming.ingrs.hdwallet.PinActivity

import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.BitcoinAPI
import com.gaming.ingrs.hdwallet.backend.Cryptography
import com.gaming.ingrs.hdwallet.backend.Operations

/**
 * A simple [Fragment] subclass.
 */
class NewWalletFragment : Fragment() {

    companion object{

        lateinit var new_wallet_loading_spinner: ProgressBar
        lateinit var newWalletTitle: TextView
        lateinit var newWalletDescription: TextView
        lateinit var newWalletVerifyButton: Button
        lateinit var seedPhrase: TextView
        lateinit var newWalletImage: ImageView

        val SEED_LOC = "tempSeedPhrase"

        var buttonOption = 1
        var seed: List<String> = listOf("")
        var seedString: String = ""

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        setupButton()
    }

    private fun init(){
        new_wallet_loading_spinner = requireActivity().findViewById(R.id.new_wallet_loading_spinner)
        newWalletTitle = requireActivity().findViewById(R.id.newWalletTitle)
        newWalletDescription = requireActivity().findViewById(R.id.newWalletDescription)
        newWalletVerifyButton = requireActivity().findViewById(R.id.newWalletVerifyButton)
        seedPhrase = requireActivity().findViewById(R.id.seedPhrase)
        newWalletImage = requireActivity().findViewById(R.id.newWalletImage)

        new_wallet_loading_spinner.visibility = View.INVISIBLE
    }

    private fun setupButton(){
        newWalletVerifyButton.setOnClickListener {
            when(buttonOption){
                1 -> {
                    new_wallet_loading_spinner.visibility = View.VISIBLE
                    newWalletTitle.visibility = View.INVISIBLE
                    newWalletImage.visibility = View.INVISIBLE
                    newWalletDescription.visibility = View.INVISIBLE

                    newWalletDescription.text = getString(R.string.new_wallet_notice)

                    val runnable = Runnable {
                        seed = BitcoinAPI().mnemonicGenerator()
                        seedString = Operations().convertListToString(seed, " ")
                        seedPhrase.text = seedString
                        new_wallet_loading_spinner.visibility = View.INVISIBLE
                    }
                    Handler().postDelayed(runnable, 2000)
                    newWalletVerifyButton.text = getString(R.string.next)
                    buttonOption = 2
                }
                2 -> {
                    val secretKey = Cryptography().generateSecretKey(SEED_LOC)
                    val encryptedTempPin = Cryptography().encryptMsg(seedString, secretKey)
                    activity?.let { it1 ->
                        Operations().saveHashMap(SEED_LOC, encryptedTempPin,
                            it1
                        )
                    }
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.welcome_container, NewWalletConfirmFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

}
