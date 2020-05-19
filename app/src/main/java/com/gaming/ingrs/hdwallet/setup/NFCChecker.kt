package com.gaming.ingrs.hdwallet.setup

import android.content.Context
import android.nfc.NfcManager

class NFCChecker {
    companion object {
    // 1 - Enabled, 2 - Disabled, 3 - Not exist
        fun readNFCstatus(context: Context) : NFCstatus {
            val manager = context.getSystemService(Context.NFC_SERVICE) as NfcManager
            val adapter = manager.defaultAdapter
            when {
                adapter == null -> return NFCstatus.NOT_EXISTING
                adapter.isEnabled -> return NFCstatus.ENABLED
                !adapter.isEnabled -> return NFCstatus.DISABLED
            }
            return NFCstatus.NOT_EXISTING
        }
    }

    enum class NFCstatus {
        NOT_EXISTING,
        ENABLED,
        DISABLED
    }
}
