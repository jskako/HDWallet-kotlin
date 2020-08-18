package com.gaming.ingrs.hdwallet.setup

import android.content.Context
import android.nfc.NfcManager

class NFCChecker {
    // 1 - Enabled, 2 - Disabled, 3 - Not exist
        fun readNFCstatus(context: Context) : String {
            val manager = context.getSystemService(Context.NFC_SERVICE) as NfcManager
            val adapter = manager.defaultAdapter
            when {
                adapter == null -> return "NOT_EXISTING"
                adapter.isEnabled -> return "ENABLED"
                !adapter.isEnabled -> "DISABLED"
            }
            return "NOT_EXISTING"
        }
}
