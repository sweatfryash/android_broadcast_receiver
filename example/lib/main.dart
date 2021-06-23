import 'dart:async';
import 'package:flutter/material.dart';
import 'package:android_broadcast_receiver/android_broadcast_receiver.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance!.addPostFrameCallback((timeStamp) {
      AndroidBroadcastReceiver.setHandler((call) {
        print(call);
        return Future.value(null);
      });
      AndroidBroadcastReceiver.addBroadCastReceiver(
          'android.intent.action.AIRPLANE_MODE');
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: TextButton(
            onPressed: () {
              AndroidBroadcastReceiver.sendBroadCast('com.hnsi.broadcast_test',
                  <String, String>{'token': 'xxxxaaaa1111'});
            },
            child: Text('send broadcast'),
          ),
        ),
      ),
    );
  }
}
