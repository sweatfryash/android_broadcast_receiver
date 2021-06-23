package com.hnsi.android_broadcast_receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** AndroidBroadcastReceiverPlugin */
class AndroidBroadcastReceiverPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private lateinit var channel: MethodChannel
    private lateinit var activity: Activity
    private val receivers: MutableMap<String, MyBroadCastReceiver> = mutableMapOf()

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "android_broadcast_receiver")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "addReceiver" -> {
                val intentFilter = IntentFilter()
                intentFilter.addAction(call.arguments as String)
                val myBroadCastReceiver = MyBroadCastReceiver(call.arguments as String)
                activity.registerReceiver(myBroadCastReceiver, intentFilter)
                receivers[call.arguments as String] = myBroadCastReceiver
                result.success("");
            }
            "removeReceiver" -> {
                activity.unregisterReceiver(receivers[call.arguments as String])
                receivers.remove(call.arguments as String)
                result.success("");
            }
            "sendBroadcast" -> {
                val intent = Intent(call.argument<String>("action")!!)
                val extras: Map<String,String> = call.argument("extras")!!
                for((key,value) in extras){
                    intent.putExtra(key,value)
                }
                intent.setPackage(activity.packageName)
                activity.sendBroadcast(intent)
                result.success("");
            }
            else -> result.notImplemented();
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)

    }

    override fun onAttachedToActivity(p0: ActivityPluginBinding) {
        activity = p0.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {

    }

    override fun onReattachedToActivityForConfigChanges(p0: ActivityPluginBinding) {
        onAttachedToActivity(p0)
    }

    override fun onDetachedFromActivity() {
        for (receiver in receivers.values) {
            activity.unregisterReceiver(receiver)
        }
    }

    inner class MyBroadCastReceiver(private val action: String) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            activity.runOnUiThread {
                if (intent != null) {
                    channel.invokeMethod(
                        "onReceiveBroadcast",
                        mapOf("action" to action, "data" to intent.dataString)
                    )
                } else {
                    channel.invokeMethod(
                        "onReceiveBroadcast",
                        mapOf("action" to action, "data" to "")
                    )
                }
            }
        }
    }

}
