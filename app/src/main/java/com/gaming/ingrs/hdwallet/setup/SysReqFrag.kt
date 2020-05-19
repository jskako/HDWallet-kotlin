package com.gaming.ingrs.hdwallet.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gaming.ingrs.hdwallet.R

class SysReqFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_system_requirements, container, false)
    }

    private val checkNFC = Runnable {
        val state = when (NFCChecker.readNFCstatus(this@SysReqFrag.requireContext())) {
            NFCChecker.NFCstatus.ENABLED -> State.NFCAvailable
            NFCChecker.NFCstatus.DISABLED -> State.NFCAvailable
            NFCChecker.NFCstatus.NOT_EXISTING -> State.NFCUnavailable
        }
    }

    private enum class State {
        NFCAvailable,
        NFCUnavailable
    }
}