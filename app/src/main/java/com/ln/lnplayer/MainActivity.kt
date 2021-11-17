package com.ln.lnplayer
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

//import android.app.Fragment
//import android.content.Intent
//import android.os.Bundle
//import com.google.android.material.snackbar.Snackbar
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import android.widget.Button
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.FrameLayout
//import com.google.android.material.bottomnavigation.BottomNavigationMenu
//import com.ln.lnplayer.databinding.ActivityMainBinding
//import com.ln.lnplayer.fragments.*
//import android.util.Log
//
//class MainActivity : AppCompatActivity() {
//
//    private val homeFragment = HomeFragment()
//    private val settingsFragment = SettingsFragment()
//    private val weatherFragment = WeatherFragment()
//    private val libraryFragment = LibraryFragment()
//    private val mapsFragment = MapsFragment()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        findViewById<Button>(R.id.btn_login).setOnClickListener {
//            val intent = Intent(this, GoogleSignInActivity::class.java)
//            // start your next activity
//            startActivity(intent)
//        }
//
//
//        replaceFragment(settingsFragment)
//
//        var bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
//        var frame = findViewById<FrameLayout>(R.id.fragment_container)
//
//
//        bottom_nav.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.ic_home -> replaceFragment(homeFragment)
//                R.id.ic_maps -> replaceFragment(mapsFragment)
//                R.id.ic_weather -> replaceFragment(weatherFragment)
//                R.id.ic_library -> replaceFragment(libraryFragment)
//                R.id.ic_settings -> replaceFragment(settingsFragment)
//            }
//            true
//        }
//    }
//
//    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
//        if (fragment != null){
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_container, fragment)
//            transaction.commit()
//        }
//    }
//
//}

private const val WEB_CLIENT_ID = "845737301013-if52ebn3tvtlldrqvhqgmn4ar3usucjg.apps.googleusercontent.com"

class MainActivity : AppCompatActivity() {
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    //private lateinit var binding: ActivityGoogleSignIn2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
//        // Configure sign-in to request the user's ID, not email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        val gso = GoogleSignInOptions
//            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(WEB_CLIENT_ID)
//            .requestEmail()
//            .build()
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // initialize the firebaseAuth variable

        Log.d(TAG, "ONCREATE RODDOUUUUUUUUUUU???????????????????????????????")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        createRequest()

        findViewById<SignInButton>(R.id.google_signI).visibility = View.VISIBLE

        findViewById<SignInButton>(R.id.google_signI).setOnClickListener { signIn() }
    }

    private fun createRequest() {
        // Configure sign-in to request the user's ID, not email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //val account: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)

    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "A activity foi pausada.")
    }

    override fun onStop() {
        super.onStop()
        findViewById<SignInButton>(R.id.google_signI).visibility = View.GONE
        Log.d(TAG, "A activity foi matada.")
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "firebaseAuthWithGoogle: Login failed due to ${e.message}")
                //Toast.makeText(this@GoogleSignInActivity, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@MainActivity, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    // [END auth_with_google]


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun updateUI(user: FirebaseUser?) {
        user?.let {

            Log.d(TAG, "já logado: ${user}")
            val intent = Intent(this, PageManager::class.java)
            intent.putExtra("Username", user)
            // start your next activity
            //View.INVISIBLE
            findViewById<SignInButton>(R.id.google_signI).visibility = View.GONE
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            Log.d(TAG, "user: ${name}; ${email}; ${photoUrl}")

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid

            startActivity(intent);

        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 1
    }
}