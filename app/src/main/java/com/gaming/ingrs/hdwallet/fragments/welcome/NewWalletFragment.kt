package com.gaming.ingrs.hdwallet.fragments.welcome

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.BitcoinAPI
import com.gaming.ingrs.hdwallet.backend.Cryptography
import com.gaming.ingrs.hdwallet.backend.Operations
import com.gaming.ingrs.hdwallet.backend.hideKeyboard
import kotlinx.android.synthetic.main.warning_dialog.view.*

/**
 * A simple [Fragment] subclass.
 */
class NewWalletFragment : Fragment() {

    lateinit var new_wallet_loading_spinner: ProgressBar
    lateinit var newWalletTitle: TextView
    lateinit var newWalletDescription: TextView
    lateinit var newWalletVerifyButton: Button
    lateinit var seedPhrase: TextView
    lateinit var newWalletImage: ImageView

    companion object{
        const val SEED_LOC = "tempSeedPhrase"
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
        activity?.let { Operations().writeToSharedPreferences(it, "walletButtonOption", "1") }
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
            hideKeyboard()
            when(activity?.let { it1 -> Operations().readFromSharedPreferences(it1, "walletButtonOption") }){
                "1" -> {
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
                    activity?.let { Operations().writeToSharedPreferences(it, "walletButtonOption", "2") }
                }
                "2" -> {
                    //Inflate the dialog with custom view
                    val mDialogView = LayoutInflater.from(context).inflate(R.layout.warning_dialog, null)
                    //AlertDialogBuilder
                    val mBuilder = AlertDialog.Builder(context)
                        .setView(mDialogView)
                    //show dialog
                    val  mAlertDialog = mBuilder.show()
                    //login button click of custom layout
                    mDialogView.dialogConfirmBtn.setOnClickListener {
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
                        mAlertDialog.dismiss()
                    }
                    //cancel button click of custom layout
                    mDialogView.dialogCancelBtn.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }
            }
        }
    }

}
