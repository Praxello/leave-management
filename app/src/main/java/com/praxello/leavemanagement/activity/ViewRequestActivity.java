package com.praxello.leavemanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.adapter.AdminViewRequestAdapter;
import com.praxello.leavemanagement.adapter.ViewRequestAdapter;
import com.praxello.leavemanagement.model.login.LoginResponse;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponse;
import com.praxello.leavemanagement.services.ApiRequestHelper;
import com.praxello.leavemanagement.services.SmartQuiz;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRequestActivity extends AppCompatActivity {

    @BindView(R.id.rvViewStatus)
    RecyclerView rvViewStatus;
    public  static SmartQuiz smartQuiz;
    private static String TAG="ViewRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation...
        initViews();

        //load requested data...
        loadData();
    }

    private void initViews() {
        Toolbar toolbar=findViewById(R.id.toolbar_view_status);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("View Request");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        rvViewStatus.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData(){

        smartQuiz.getApiRequestHelper().getAllLeaveDetails(CommonMethods.getPrefrence(ViewRequestActivity.this,AllKeys.USER_ID),new ApiRequestHelper.OnRequestComplete() {
                    @Override
                    public void onSuccess(Object object) {
                        ViewStatusResponse viewStatusResponse=(ViewStatusResponse) object;

                    //    Log.e(TAG, "onSuccess: "+viewStatusResponse.getResponsecode());
                        Log.e(TAG, "onSuccess: "+viewStatusResponse.getMessage());
                        //Log.e(TAG, "onSuccess: "+viewStatusResponse.getData());

                        if(viewStatusResponse.getResponsecode()==200){
                            if(viewStatusResponse.getData()!=null){
                                if(getIntent().getStringExtra("type").equals("user")){
                                    ViewRequestAdapter viewRequestAdapter=new ViewRequestAdapter(viewStatusResponse.getData(),ViewRequestActivity.this);
                                    rvViewStatus.setAdapter(viewRequestAdapter);
                                }else if(getIntent().getStringExtra("type").equals("admin")){
                                    AdminViewRequestAdapter adminViewRequestAdapter=new AdminViewRequestAdapter(viewStatusResponse.getData(),ViewRequestActivity.this);
                                    rvViewStatus.setAdapter(adminViewRequestAdapter);
                                }

                            }
                        }else{
                            Toast.makeText(ViewRequestActivity.this, viewStatusResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String apiResponse) {
                        Toast.makeText(ViewRequestActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }
}
