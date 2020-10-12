package com.gaming.ingrs.hdwallet.fragments.welcome

import PasswordManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.Operations
import com.github.tntkhang.gmailsenderlibrary.GMailSender
import com.github.tntkhang.gmailsenderlibrary.GmailListener


/**
 * A simple [Fragment] subclass.
 */
class MailFragment : Fragment() {

    companion object{
        lateinit var mailTitle: TextView
        lateinit var mailDescription: TextView
        lateinit var enteredMail: EditText
        lateinit var mailButton: Button
        lateinit var mailButtonVerify: Button
        lateinit var verifyMail: TextView

        var MAIL = "hdwalletapp@gmail.com"
        var MAIL_PASSWORD = "_5n{mT(5c@~3Z*\"S"
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
        mailButtonVerify = requireActivity().findViewById(R.id.mailButtonVerify)
        verifyMail = requireActivity().findViewById(R.id.verifyMail)

        verifyMail.visibility = View.INVISIBLE
        mailButtonVerify.visibility = View.INVISIBLE
        mailButtonVerify.isEnabled = false
    }

    private fun checkMail(){
        mailButton.setOnClickListener {
            if(Operations().isEmailValid(enteredMail.text.toString())){
                enteredMail.isEnabled = false
                verifyMail.visibility = View.VISIBLE
                mailButtonVerify.visibility = View.VISIBLE
                mailButtonVerify.isEnabled = true
                mailButton.visibility = View.INVISIBLE
                mailButton.isEnabled = false

                val myVerificationCode = codeGenerator()
                val message = "Your verification code is: $myVerificationCode"

                sendEmail(message)
            }else{
                Toast.makeText(context, "Entered mail is invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEmail(message: String) {
        GMailSender.withAccount(MAIL, MAIL_PASSWORD)
            .withTitle(getString(R.string.mail_verification_title))
            .withBody(message)
            .withSender(MAIL)
            .toEmailAddress(enteredMail.text.toString()) // one or multiple addresses separated by a comma
            .withListenner(object : GmailListener {
                override fun sendSuccess() {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                }

                override fun sendFail(err: String) {
                    Toast.makeText(context, "Fail: $err", Toast.LENGTH_SHORT).show()
                }
            })
            .send()
    }

    private fun codeGenerator(){
        var myPasswordManager = PasswordManager()
        var newPassord: String = myPasswordManager.generatePassword(
            isWithLetters = true,
            isWithUppercase = true,
            isWithNumbers = true,
            isWithSpecial = false,
            length = 6
        )
    }
}
