package com.widetech.mobile.wide_tech.DataAccess.ServiceFirebase

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("Registered")
class PushNotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("FirebaseMessage", "Llego message")
    }
}