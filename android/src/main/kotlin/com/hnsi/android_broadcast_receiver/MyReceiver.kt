package com.hnsi.android_broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("=======================")
        println(intent.getStringExtra("token"))
    }
}