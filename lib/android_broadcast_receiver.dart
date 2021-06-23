import 'dart:async';
import 'package:flutter/services.dart';

class AndroidBroadcastReceiver {
  static const MethodChannel _channel =
      const MethodChannel('android_broadcast_receiver');

  static Future<void> addBroadCastReceiver(String action) async {
    await _channel.invokeMethod('addReceiver', action);
  }

  static Future<void> sendBroadCast(
      String action, Map<String, String> extras) async {
    await _channel
        .invokeMethod('sendBroadcast', {'action': action, 'extras': extras});
  }

  static Future<void> removeBroadCastReceiver(String action) async {
    await _channel.invokeMethod('removeReceiver', action);
  }

  static void setHandler(Future<dynamic> Function(MethodCall call) handler) {
    _channel.setMethodCallHandler(handler);
  }
}
