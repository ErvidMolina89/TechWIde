package com.widetech.mobile.wide_tech.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.widetech.mobile.wide_tech.Base.App
import com.widetech.mobile.wide_tech.Base.BaseActivity
import com.widetech.mobile.wide_tech.Models.Login
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.hiddenProgress
import com.widetech.mobile.wide_tech.Utils.showProgress
import com.widetech.mobile.wide_tech.rest.Endpoints.PutExtra.RC_SIGN_IN
import com.widetech.mobile.wide_tech.ui.dash.MainActivity
import com.widetech.mobile.wide_tech.ui.dashboard.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : BaseActivity() {
    private var login: Button? = null

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        val username = findViewById<EditText>(R.id.et_username)
        val password = findViewById<EditText>(R.id.et_password)
        login = findViewById<Button>(R.id.btn_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        listenerViewModel()

    }

    private fun listenerViewModel(){
        login?.setOnClickListener {
            App.mContext?.showProgress()
            val user = Login()
            loginViewModel.login(user)
        }
        loginViewModel.loginResult.observeForever {
            val result = it
            if (result.UserName != "") {
                nextActivity()
            }
        }
        btn_google.setOnClickListener {
            App.mContext?.showProgress()
            val googleSignInClient = GoogleSignIn.getClient(this, App.gso!!)
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                nextActivity()
                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d("Google", "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e("Google", "Google sign in failed", e)
            }
        }
    }

    private fun nextActivity(){
        App.mContext?.hiddenProgress()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("FirebaseAuth", "signInWithCredential:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("FirebaseAuth", "signInWithCredential:failure", task.exception)
                    // ...
                    //Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }
}
