package com.gaming.ingrs.hdwallet.backend

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import com.gaming.ingrs.hdwallet.R
import kotlinx.android.synthetic.main.custom_dialog.view.*


class Operations {

    fun convertListToString(
        words: List<String>,
        delimiter: String
    ): String {
        val builder = StringBuilder()
        for (details in words) {
            builder.append(details)
            builder.append(delimiter)
        }
        return builder.toString()
    }

    fun writeToSharedPreferences(activity: Activity, key: String, data: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, data)
            commit()
        }
    }

    fun readFromSharedPreferences(activity: Activity, key: String): String? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, "0")
    }

    fun showCreateCategoryDialog(context: Context) {
        val alert = AlertDialog.Builder(context)
            .setTitle("Title")
            .setMessage("Message")
        val input = EditText(context)
        alert.setView(input)
        alert.setPositiveButton("Ok") { dialog, whichButton ->
            val value: String = input.text.toString()
            Log.e("123", "$value")
        }
        alert.setNegativeButton(
            "Cancel"
        ) { dialog, whichButton ->
            // Canceled.
        }
        alert.show()
    }

    fun customDialog(context: Context) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Enter PIN")
        val mAlertDialog = mBuilder.show()
        mDialogView.dialogLoginBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            val name = mDialogView.dialogNameEt.text.toString()
            val email = mDialogView.dialogEmailEt.text.toString()
            val password = mDialogView.dialogPasswEt.text.toString()
            //set the input text in TextView
            Log.e("123", "Name:$name\nEmail: $email\nPassword: $password")
        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }
}