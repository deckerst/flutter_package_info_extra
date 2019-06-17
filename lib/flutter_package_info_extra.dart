import 'dart:async';
import 'dart:typed_data';

import 'package:convert/convert.dart';
import 'package:flutter/services.dart';

class FlutterPackageInfoExtra {
  static const MethodChannel _channel = const MethodChannel('flutter_package_info_extra');

  static Future<List<String>> get signingCertificates async {
    List<dynamic> digests = await _channel.invokeMethod('getSigningCertificates');
    List<Uint8List> digestBytes = digests.cast<Uint8List>();
    List<String> signatures = digestBytes.map((byteArray) => hex.encode(byteArray).replaceAllMapped(new RegExp(r".{2}(?!$)"), (match) => '${match.group(0)}:').toUpperCase()).toList();
    return signatures;
  }
}
