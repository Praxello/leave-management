package com.praxello.leavemanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_DURATION = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
}
