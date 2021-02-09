package com.gaming.ingrs.hdwallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        changeActionBar(R.color.welcome_background)
    }

    private fun setToolbar(){
        //Set toolbar
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.startingFragment)
        NavigationUI.setupWithNavController(navigation_view, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
    }

    private fun changeActionBar(resourceColor: Int){
        window.statusBarColor = ContextCompat.getColor(applicationContext, resourceColor)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    override fun onBackPressed() {
        // Disable back button, do nothing
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
