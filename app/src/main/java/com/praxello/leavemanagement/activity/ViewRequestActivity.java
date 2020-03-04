package com.praxello.leavemanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.adapter.AdminViewRequestAdapter;
import com.praxello.leavemanagement.adapter.ViewRequestAdapter;
import com.praxello.leavemanagement.model.login.LoginResponse;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusData;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponse;
import com.praxello.leavemanagement.services.ApiRequestHelper;
import com.praxello.leavemanagement.services.SmartQuiz;
import com.praxello.leavemanagement.widgets.slidingitemrecyclerview.SlidingItemMenuRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRequestActivity extends AppCompatActivity {

    @BindView(R.id.rvViewStatus)
    SlidingItemMenuRecyclerView rvViewStatus;
    public  static SmartQuiz smartQuiz;
    private static String TAG="ViewRequestActivity";
    @BindView(R.id.ll_spin)
    LinearLayout llSpin;
    @BindView(R.id.spin_list_type)
    Spinner spinListType;
    public static ArrayList<ViewStatusData> viewStatusDataArrayList=new ArrayList<>();
    ArrayList<ViewStatusData> pendingArrayList=new ArrayList<>();
    ArrayList<ViewStatusData> approvedArrayList=new ArrayList<>();
    ArrayList<ViewStatusData> rejectArrayList=new ArrayList<>();
    static ViewRequestAdapter viewRequestAdapter;

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

        if(getIntent().getStringExtra("type").equals("admin")){
            llSpin.setVisibility(View.VISIBLE);
            String[] leaveType = {"All","Approved", "Reject", "Pending"};
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, leaveType);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinListType.setAdapter(arrayAdapter);

            spinListType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(viewStatusDataArrayList!=null && position==0){
                        AdminViewRequestAdapter adminViewRequestAdapter=new AdminViewRequestAdapter(viewStatusDataArrayList,ViewRequestActivity.this);
                        rvViewStatus.setAdapter(adminViewRequestAdapter);
                    }

                    if(approvedArrayList!=null && position==1){
                        AdminViewRequestAdapter adminViewRequestAdapter=new AdminViewRequestAdapter(approvedArrayList,ViewRequestActivity.this);
                        rvViewStatus.setAdapter(adminViewRequestAdapter);
                    }

                    if(rejectArrayList!=null && position==2){
                        AdminViewRequestAdapter adminViewRequestAdapter=new AdminViewRequestAdapter(rejectArrayList,ViewRequestActivity.this);
                        rvViewStatus.setAdapter(adminViewRequestAdapter);
                    }

                    if(pendingArrayList!=null && position==3){
                        AdminViewRequestAdapter adminViewRequestAdapter=new AdminViewRequestAdapter(pendingArrayList,ViewRequestActivity.this);
                        rvViewStatus.setAdapter(adminViewRequestAdapter);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    private void initViews() {
        Toolbar toolbar=findViewById(R.id.toolbar_view_status);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("View Request");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewRequestActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvViewStatus.setLayoutManager(linearLayoutManager);
    }

    private void loadData(){
        final ProgressDialog progress = new ProgressDialog(ViewRequestActivity.this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);
        smartQuiz.getApiRequestHelper().getAllLeaveDetails(CommonMethods.getPrefrence(ViewRequestActivity.this,AllKeys.USER_ID),new ApiRequestHelper.OnRequestComplete() {
                    @Override
                    public void onSuccess(Object object) {
                        ViewStatusResponse viewStatusResponse=(ViewStatusResponse) object;

                        //Log.e(TAG, "onSuccess: "+viewStatusResponse.getResponsecode());
                        Log.e(TAG, "onSuccess: "+viewStatusResponse.getMessage());
                        //Log.e(TAG, "onSuccess: "+viewStatusResponse.getData());
                        progress.dismiss();
                        if(viewStatusResponse.getResponsecode()==200){
                            if(viewStatusResponse.getData()!=null){
                                viewStatusDataArrayList=viewStatusResponse.getData();

                                for(int i=0;i<viewStatusResponse.getData().size();i++){
                                    if(viewStatusResponse.getData().get(i).getLeave_status().equals("pending")){
                                        pendingArrayList.add(viewStatusResponse.getData().get(i));
                                    }

                                    if(viewStatusResponse.getData().get(i).getLeave_status().equals("reject")){
                                        rejectArrayList.add(viewStatusResponse.getData().get(i));
                                    }

                                    if(viewStatusResponse.getData().get(i).getLeave_status().equals("approved")){
                                        approvedArrayList.add(viewStatusResponse.getData().get(i));
                                    }
                                }
                                if(getIntent().getStringExtra("type").equals("user")){
                                    viewRequestAdapter=new ViewRequestAdapter(viewStatusDataArrayList,ViewRequestActivity.this);
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
                        progress.dismiss();
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
