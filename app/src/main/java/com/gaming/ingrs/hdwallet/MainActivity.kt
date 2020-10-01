package com.gaming.ingrs.hdwallet

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        private lateinit var navController: NavController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
    }

    private fun setToolbar(){
        //Set toolbar
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.startingFragment)
        NavigationUI.setupWithNavController(navigation_view, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

        //Hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    /*private fun test() {
        val activity: Activity = this
        val textView = findViewById<TextView>(R.id.test)
        val textViewOne = findViewById<TextView>(R.id.textView)
        val textViewTwo = findViewById<TextView>(R.id.textView2)
        val bc = BitcoinAPI()
        val words: List<String> = bc.mnemonicGenerator()
        val abs = Operations()
        val myWords: String = abs.convertListToString(words, " ")
        textView.text = myWords

        val seed = bc.seedGenerator(words, "")
        val mkey = bc.generateMasterKey(seed)
        Log.e("Privatekey",mkey.privateKeyAsHex)
        Log.e("Publickey",mkey.publicKeyAsHex)
        val derbcr = bc.deriveBitcoinRootKey(mkey)
        Log.e("Derbcr",derbcr.toString())

        abs.writeToSharedPreferences(this,"Test","This is my TEST data")
        val rfsp = abs.readFromSharedPreferences(this, "Test")
        textView.text = rfsp
        val fragment = SafetynetAttestation()
        fragment.sendSafetyNetRequest(activity)

        val nfc: NFCChecker? = null
        val nfcAvailable = nfc!!.readNFCstatus(this)
        Log.d(TAG, "NFC: $nfcAvailable")

    }*/
}
