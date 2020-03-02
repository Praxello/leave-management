package com.praxello.leavemanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminDashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ll_logout)
    LinearLayout llLogOut;
    @BindView(R.id.ll_view_request)
    LinearLayout llViewRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);
        ButterKnife.bind(this);

        //basic intialisation....
        initViews();
    }

    private void initViews(){
         llLogOut.setOnClickListener(this);
         llViewRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_view_request:
                Intent intent = new Intent(AdminDashBoardActivity.this,ViewRequestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type","admin");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                break;

            case R.id.ll_logout:
                new android.app.AlertDialog.Builder(AdminDashBoardActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Are you sure you want to logout?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.USER_ID, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.FIRST_NAME, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.LAST_NAME, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.MOBILE, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.EMAIL, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.CITY, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.STATE, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.COUNTRY, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.PINCODE, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.DATEOFBIRTH, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.ADDRESS, AllKeys.DNF);
                                CommonMethods.setPreference(AdminDashBoardActivity.this, AllKeys.LOGINTYPE, AllKeys.DNF);
                                Intent intent = new Intent(AdminDashBoardActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                                Toast.makeText(AdminDashBoardActivity.this, "See you again!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
        }
    }
}
