package com.praxello.leavemanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_DURATION = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();


    }

    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            /* New Handler to start the Menu-Activity
                             * and close this Splash-Screen after some seconds.*/
                            // Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    /* Create an Intent that will start the Menu-Activity. */
                                    if(CommonMethods.getPrefrence(MainActivity.this, AllKeys.USER_ID).equals(AllKeys.DNF)){
                                        Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                                    }else{
                                        if(CommonMethods.getPrefrence(MainActivity.this,AllKeys.LOGINTYPE).equals("0")){
                                            Intent mainIntent = new Intent(MainActivity.this, AdminDashBoardActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                                        }else{
                                            Intent mainIntent = new Intent(MainActivity.this, DashBoardActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                                        }
                                    }
                                }
                            }, SPLASH_DISPLAY_DURATION);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
