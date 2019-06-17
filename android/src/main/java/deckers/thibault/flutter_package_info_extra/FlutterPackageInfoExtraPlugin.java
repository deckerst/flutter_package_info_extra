package deckers.thibault.flutter_package_info_extra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class FlutterPackageInfoExtraPlugin implements MethodCallHandler {
    private final Registrar mRegistrar;

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_package_info_extra");
        channel.setMethodCallHandler(new FlutterPackageInfoExtraPlugin(registrar));
    }

    private FlutterPackageInfoExtraPlugin(Registrar registrar) {
        this.mRegistrar = registrar;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getSigningCertificates")) {
            Context context = mRegistrar.context();
            result.success(getSignatureDigests(context));
        } else {
            result.notImplemented();
        }
    }

    private static List<byte[]> getSignatureDigests(Context context) {
        List<byte[]> signatureDigests = new ArrayList<>();
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            Signature[] signatures = null;
            if (Build.VERSION.SDK_INT >= 28) {
                final PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
                if (packageInfo != null)
                    signatures = packageInfo.signingInfo.getApkContentsSigners();
            } else {
                @SuppressLint("PackageManagerGetSignatures") final PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                if (packageInfo != null) signatures = packageInfo.signatures;
            }
            if (signatures != null) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                for (Signature signature : signatures) {
                    signatureDigests.add(md.digest(signature.toByteArray()));
                }
            }

        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signatureDigests;
    }
}
