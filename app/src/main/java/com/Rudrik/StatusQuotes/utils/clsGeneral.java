package com.Rudrik.StatusQuotes.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Rudrik on 5/15/2014.
 */
public class clsGeneral {

    public void generateHashKeyForFacebook(Context context) throws Exception {
        try {
            PackageInfo _info = null;
            _info = context.getPackageManager().getPackageInfo("com.Rudrik.StatusQuotes", PackageManager.GET_SIGNATURES);
            if (_info == null) {
                Toast.makeText(context.getApplicationContext(), "Invalid Package Name / Package not found", Toast.LENGTH_LONG).show();
                return;
            }
            for (Signature signature : _info.signatures) {
                MessageDigest _md = MessageDigest.getInstance("SHA");
                _md.update(signature.toByteArray());
                Log.d("FB KeyHash: =>", Base64.encodeToString(_md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
