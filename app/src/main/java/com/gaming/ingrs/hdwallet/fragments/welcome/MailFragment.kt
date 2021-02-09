package com.gaming.ingrs.hdwallet.fragments.welcome

import PasswordManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.Operations
import com.gaming.ingrs.hdwallet.backend.hideKeyboard
import com.github.tntkhang.gmailsenderlibrary.GMailSender
import com.github.tntkhang.gmailsenderlibrary.GmailListener


/**
 * A simple [Fragment] subclass.
 */
class MailFragment : Fragment() {

    lateinit var mailTitle: TextView
    lateinit var mailDescription: TextView
    lateinit var enteredMail: EditText
    lateinit var mailButton: Button
    lateinit var resendVerCode: Button
    lateinit var progressBar: ProgressBar
    lateinit var mailImage: ImageView

    companion object{
        var MAIL = "hdwalletapp@gmail.com"
        var MAIL_PASSWORD = "_5n{mT(5c@~3Z*\"S"
        var mailButtonCode = 1
        var message: String = ""
        var myVerificationCode: String = ""
        var recMail: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        checkMail()
    }

    private fun init(){
        mailTitle = requireActivity().findViewById(R.id.mailTitle)
        mailDescription = requireActivity().findViewById(R.id.mailDescription)
        enteredMail = requireActivity().findViewById(R.id.enteredMail)
        mailButton = requireActivity().findViewById(R.id.mailButton)
        resendVerCode = requireActivity().findViewById(R.id.resendVerCode)
        progressBar = requireActivity().findViewById(R.id.loading_spinner)
        mailImage = requireActivity().findViewById(R.id.mailImage)

        progressBar.visibility = View.INVISIBLE
        resendVerCode.visibility = View.INVISIBLE
        resendVerCode.isEnabled = false
    }

    private fun checkMail(){
        mailButton.setOnClickListener {
            when(mailButtonCode){
                1-> {
                    if(Operations().isEmailValid(enteredMail.text.toString())){
                        hideKeyboard()
                        progressBar.visibility = View.VISIBLE
                        recMail = enteredMail.text.toString()
                        setMailMessage()
                        sendEmail(message)
                    }else{
                        Toast.makeText(context, "Entered mail is invalid", Toast.LENGTH_SHORT).show()
                    }
                }
                2 -> {
                    setResendButton()
                    if(myVerificationCode.equals(enteredMail.text.toString())){
                        hideKeyboard()
                        progressBar.visibility = View.VISIBLE
                        hideAllElements()
                        mailDescription.setTextColor(resources.getColor(R.color.white))
                        val runnable = Runnable {
                            activity?.let { it1 -> Operations().writeToSharedPreferences(it1,"userMail", recMail) }
                            requireFragmentManager().beginTransaction()
                                .replace(R.id.welcome_container, WalletFragment())
                                .addToBackStack(null)
                                .commit()
                            progressBar.visibility = View.INVISIBLE
                        }
                        Handler().postDelayed(runnable, 2000)

                    } else{
                        mailTitle.text = getString(R.string.mail_title_error)
                        mailDescription.text = getString(R.string.incorrect_verification_code)
                        mailDescription.setTextColor(resources.getColor(R.color.red))
                    }
                }
            }
        }
    }

    private fun hideAllElements(){
        mailTitle.visibility = View.INVISIBLE
        mailDescription.visibility = View.VISIBLE
        enteredMail.visibility = View.INVISIBLE
        mailButton.visibility = View.INVISIBLE
        resendVerCode.visibility = View.INVISIBLE
        mailImage.visibility = View.INVISIBLE

        mailDescription.text = getString(R.string.processing_data)
    }

    private fun setResendButton(){
        resendVerCode.setOnClickListener {
            hideKeyboard()
            setMailMessage()
            sendEmail(message)
        }
    }

    private fun setMailMessage(){
        myVerificationCode = codeGenerator()
        message = "Your verification code is: $myVerificationCode"
    }

    private fun sendEmail(message: String) {
        GMailSender.withAccount(MAIL, MAIL_PASSWORD)
            .withTitle(getString(R.string.mail_verification_title))
            .withBody(message)
            .withSender(MAIL)
            .toEmailAddress(recMail) // one or multiple addresses separated by a comma
            .withListenner(object : GmailListener {
                override fun sendSuccess() {
                    progressBar.visibility = View.INVISIBLE
                    mailTitle.text = getString(R.string.confirmation)
                    resendVerCode.visibility = View.VISIBLE
                    resendVerCode.isEnabled = true
                    mailButton.text = getString(R.string.verification_button)
                    val enteredMailText = enteredMail.text.toString()
                    mailDescription.text = getString(R.string.verif_mail_desc,  recMail)
                    enteredMail.setText("")

                    setResendButton()
                    mailButtonCode = 2
                    Toast.makeText(context, "Email sent successfully", Toast.LENGTH_SHORT).show()
                }

                override fun sendFail(err: String) {
                    Toast.makeText(context, "Fail: $err", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
            })
            .send()
    }

    private fun codeGenerator(): String{
        val myPasswordManager = PasswordManager()
        return myPasswordManager.generatePassword(
            isWithLetters = true,
            isWithUppercase = true,
            isWithNumbers = true,
            isWithSpecial = false,
            length = 6
        )
    }
}
