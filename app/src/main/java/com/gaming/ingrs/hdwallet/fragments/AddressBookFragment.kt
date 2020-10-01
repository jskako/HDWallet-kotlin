package com.gaming.ingrs.hdwallet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.gaming.ingrs.hdwallet.R
import com.gaming.ingrs.hdwallet.backend.RegisterSwipeTouchListener

/**
 * A simple [Fragment] subclass.
 */
class AddressBookFragment : Fragment() {

    private lateinit var layout: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_book, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSwipeListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSwipeListener(){
        layout = requireView().findViewById(R.id.addressBookLayout) as FrameLayout
        val swipe = RegisterSwipeTouchListener()
        swipe.setTouchListener(requireContext(), layout, requireView())
    }

}
