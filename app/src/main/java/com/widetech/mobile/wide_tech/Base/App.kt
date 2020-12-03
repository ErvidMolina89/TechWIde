package com.widetech.mobile.wide_tech.Base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.widetech.mobile.wide_tech.R

@SuppressLint("Registered")
class App: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var mContext: Context?= null
        @SuppressLint("StaticFieldLeak")
        var gso: GoogleSignInOptions? = null
        private var instance : App ?= null
        fun getInstance() : App{
            return instance!!
        }
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            task ->
            if (!task.isSuccessful){
                Log.w("Init Message", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("Init Message", token)
            //Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
}