package com.ln.lnplayer.fragments
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.ln.lnplayer.R
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
import com.ln.lnplayer.MainActivity

private const val WEB_CLIENT_ID = "845737301013-if52ebn3tvtlldrqvhqgmn4ar3usucjg.apps.googleusercontent.com"

class GoogleSignInActivity : Activity() {
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
        setContentView(R.layout.activity_google_sign_in2)

        auth = Firebase.auth

        createRequest()

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
        updateUI(currentUser)

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
                Toast.makeText(this@GoogleSignInActivity, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    // [END auth_with_google]


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            Log.d(TAG, "já logado: ${user}")
        }

    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 1
    }
}