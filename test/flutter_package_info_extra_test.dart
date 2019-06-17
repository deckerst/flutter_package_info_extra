import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_package_info_extra/flutter_package_info_extra.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_package_info_extra');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return ['11:22:33:44'];
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getSigningCertificate', () async {
    expect(await FlutterPackageInfoExtra.signingCertificates, ['11:22:33:44']);
  });
}
