package com.gaming.ingrs.hdwallet.fragments.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.gaming.ingrs.hdwallet.MainActivity

import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.Cryptography
import com.gaming.ingrs.hdwallet.backend.Operations
import com.gaming.ingrs.hdwallet.backend.hideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class NewWalletConfirmFragment : Fragment() {

    lateinit var word1: AutoCompleteTextView
    lateinit var word2: AutoCompleteTextView
    lateinit var word3: AutoCompleteTextView
    lateinit var word4: AutoCompleteTextView
    lateinit var word5: AutoCompleteTextView
    lateinit var word6: AutoCompleteTextView
    lateinit var word7: AutoCompleteTextView
    lateinit var word8: AutoCompleteTextView
    lateinit var word9: AutoCompleteTextView
    lateinit var word10: AutoCompleteTextView
    lateinit var word11: AutoCompleteTextView
    lateinit var word12: AutoCompleteTextView

    private lateinit var confirm_wallet_loading_spinner: ProgressBar
    lateinit var confirmWalletVerifyButton: Button
    lateinit var confirmWalletNewCode: Button
    lateinit var confirmWalletTitle: TextView
    lateinit var confirmWalletDescription: TextView

    companion object{
        const val TEMP_SEED_LOC = "tempSeedPhrase"
        const val SEED_LOC = "seedPhrase"
        var decryptTempSeed: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_wallet_confirm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        getTempSeed()
        setAutoComplete()
        setupButton()
    }

    private fun setAutoComplete(){
        val autoCompleteList = Operations().convertStringToList(decryptTempSeed, " ")
        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, autoCompleteList) }
        word1.setAdapter(adapter)
        word2.setAdapter(adapter)
        word3.setAdapter(adapter)
        word4.setAdapter(adapter)
        word5.setAdapter(adapter)
        word6.setAdapter(adapter)
        word7.setAdapter(adapter)
        word8.setAdapter(adapter)
        word9.setAdapter(adapter)
        word10.setAdapter(adapter)
        word11.setAdapter(adapter)
        word12.setAdapter(adapter)
    }

    private fun getTempSeed(){
        val encryptedTempSeed =
            activity?.let { it1 -> Operations().getHashMap(TEMP_SEED_LOC, it1) }
        val secretTempSeedKey = Cryptography().getSecretKey(TEMP_SEED_LOC)
        decryptTempSeed = Cryptography().decryptMsg(encryptedTempSeed, secretTempSeedKey)?.decodeToString()!!
    }

    private fun init(){
        word1 = requireActivity().findViewById(R.id.word1)
        word2 = requireActivity().findViewById(R.id.word2)
        word3 = requireActivity().findViewById(R.id.word3)
        word4 = requireActivity().findViewById(R.id.word4)
        word5 = requireActivity().findViewById(R.id.word5)
        word6 = requireActivity().findViewById(R.id.word6)
        word7 = requireActivity().findViewById(R.id.word7)
        word8 = requireActivity().findViewById(R.id.word8)
        word9 = requireActivity().findViewById(R.id.word9)
        word10 = requireActivity().findViewById(R.id.word10)
        word11 = requireActivity().findViewById(R.id.word11)
        word12 = requireActivity().findViewById(R.id.word12)

        confirm_wallet_loading_spinner = requireActivity().findViewById(R.id.confirm_wallet_loading_spinner)
        confirmWalletVerifyButton = requireActivity().findViewById(R.id.confirmWalletVerifyButton)
        confirmWalletNewCode = requireActivity().findViewById(R.id.confirmWalletNewCode)
        confirmWalletTitle = requireActivity().findViewById(R.id.confirmWalletTitle)
        confirmWalletDescription = requireActivity().findViewById(R.id.confirmWalletDescription)

        confirm_wallet_loading_spinner.visibility = View.INVISIBLE
    }

    private fun hideElements(){
        confirmWalletVerifyButton.visibility = View.INVISIBLE
        confirmWalletNewCode.visibility = View.INVISIBLE
        confirmWalletTitle.visibility = View.INVISIBLE
        word1.visibility = View.INVISIBLE
        word2.visibility = View.INVISIBLE
        word3.visibility = View.INVISIBLE
        word4.visibility = View.INVISIBLE
        word5.visibility = View.INVISIBLE
        word6.visibility = View.INVISIBLE
        word7.visibility = View.INVISIBLE
        word8.visibility = View.INVISIBLE
        word9.visibility = View.INVISIBLE
        word10.visibility = View.INVISIBLE
        word11.visibility = View.INVISIBLE
        word12.visibility = View.INVISIBLE
    }

    private fun colorIncorrectSeed(){
        val result: List<String> = decryptTempSeed.split(" ").map { it.trim() }
        if(result[0] == word1.text.toString()){
            word1.setTextColor(resources.getColor(R.color.white))
        }else{
            word1.setTextColor(resources.getColor(R.color.red))
        }

        if(result[1] == word2.text.toString()){
            word2.setTextColor(resources.getColor(R.color.white))
        }else{
            word2.setTextColor(resources.getColor(R.color.red))
        }

        if(result[2] == word3.text.toString()){
            word3.setTextColor(resources.getColor(R.color.white))
        }else{
            word3.setTextColor(resources.getColor(R.color.red))
        }

        if(result[3] == word4.text.toString()){
            word4.setTextColor(resources.getColor(R.color.white))
        }else{
            word4.setTextColor(resources.getColor(R.color.red))
        }

        if(result[4] == word5.text.toString()){
            word5.setTextColor(resources.getColor(R.color.white))
        }else{
            word5.setTextColor(resources.getColor(R.color.red))
        }

        if(result[5] == word6.text.toString()){
            word6.setTextColor(resources.getColor(R.color.white))
        }else{
            word6.setTextColor(resources.getColor(R.color.red))
        }

        if(result[6] == word7.text.toString()){
            word7.setTextColor(resources.getColor(R.color.white))
        }else{
            word7.setTextColor(resources.getColor(R.color.red))
        }

        if(result[7] == word8.text.toString()){
            word8.setTextColor(resources.getColor(R.color.white))
        }else{
            word8.setTextColor(resources.getColor(R.color.red))
        }

        if(result[8] == word9.text.toString()){
            word9.setTextColor(resources.getColor(R.color.white))
        }else{
            word9.setTextColor(resources.getColor(R.color.red))
        }

        if(result[9] == word10.text.toString()){
            word10.setTextColor(resources.getColor(R.color.white))
        }else{
            word10.setTextColor(resources.getColor(R.color.red))
        }

        if(result[10] == word11.text.toString()){
            word11.setTextColor(resources.getColor(R.color.white))
        }else{
            word11.setTextColor(resources.getColor(R.color.red))
        }

        if(result[11] == word12.text.toString()){
            word12.setTextColor(resources.getColor(R.color.white))
        }else{
            word12.setTextColor(resources.getColor(R.color.red))
        }
    }

    private fun setupButton(){
        confirmWalletVerifyButton.setOnClickListener {
            hideKeyboard()
            confirmWalletDescription.text = getString(R.string.checking)
            val seed = "${word1.text} ${word2.text} ${word3.text} ${word4.text} ${word5.text} ${word6.text} ${word7.text} ${word8.text} ${word9.text} ${word10.text} ${word11.text} ${word12.text}"
            Log.e("", seed)
            colorIncorrectSeed()

            if (seed.trim() == decryptTempSeed.trim()){
                confirm_wallet_loading_spinner.visibility = View.VISIBLE
                hideElements()
                confirmWalletDescription.setTextColor(resources.getColor(R.color.white))
                confirmWalletDescription.text = getString(R.string.creating_wallet)
                val secretKey = Cryptography().generateSecretKey(SEED_LOC)
                val encryptedTempPin = Cryptography().encryptMsg(seed, secretKey)
                activity?.let { it1 ->
                    Operations().saveHashMap(SEED_LOC, encryptedTempPin,
                        it1
                    )
                }
                val runnable = Runnable {
                    Log.e("123","Going to end")
                    confirm_wallet_loading_spinner.visibility = View.INVISIBLE
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.welcome_container, EndingWalletFragment())
                        .addToBackStack(null)
                        .commit()
                    Log.e("123","Ended")
                }
                Handler().postDelayed(runnable, 2000)
            } else{
                confirmWalletDescription.text = getString(R.string.incorrect_seed)
                confirmWalletDescription.setTextColor(resources.getColor(R.color.red))
            }
        }

        confirmWalletNewCode.setOnClickListener {
            hideKeyboard()
            activity?.let { Operations().writeToSharedPreferences(it, "walletButtonOption", "1") }
            requireFragmentManager().popBackStack()
        }
    }

}
