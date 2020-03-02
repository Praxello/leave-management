package com.praxello.leavemanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.model.CommonResponse;
import com.praxello.leavemanagement.services.ApiRequestHelper;
import com.praxello.leavemanagement.services.SmartQuiz;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyLeaveActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.spin_leave_type)
    Spinner spinLeaveType;
    @BindView(R.id.spin_leave_request)
    Spinner spinLeaveRequest;
    @BindView(R.id.et_start_date)
    EditText etStartDate;
    @BindView(R.id.et_end_date)
    EditText etEndDate;
    @BindView(R.id.btn_startdate)
    AppCompatButton btnStartDate;
    @BindView(R.id.btn_enddate)
    AppCompatButton btnEndDate;
    @BindView(R.id.et_leave_reason)
    EditText etLeaveReason;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    private int mYear, mMonth, mDay;
    SmartQuiz smartQuiz;
    private static String TAG="ApplyLeaveActivity";
    private String stLeaveType,stLeaveRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation...
        initViews();

        //setDatatoSpinner...
        setDataToSpinner();
    }

    private void initViews() {
        Toolbar toolbar=findViewById(R.id.toolbar_apply_leave);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Apply Leave");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        btnStartDate.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_startdate:
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                etStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;

            case R.id.btn_enddate:
                // Get Current Date
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                view.setMinDate(System.currentTimeMillis() - 1000);

                                etEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

                break;

            case R.id.btn_submit:
                if(isValidated()){
                    submitLeave();
                }
                break;
        }
    }

    private void submitLeave(){
        Map<String,String> params=new HashMap<>();
        params.put("user_id", CommonMethods.getPrefrence(ApplyLeaveActivity.this, AllKeys.USER_ID));
        params.put("leave_request",stLeaveRequest);
        params.put("leave_type",stLeaveType);
        params.put("leave_reason",etLeaveReason.getText().toString());
        params.put("start_date",etStartDate.getText().toString());
        params.put("end_date",etEndDate.getText().toString());
        params.put("leave_status","pending");

        Log.e(TAG, "submitLeave: "+params );
        smartQuiz.getApiRequestHelper().addLeaveRequest(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CommonResponse commonResponse=(CommonResponse) object;

                Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+commonResponse.getMessage());

                if(commonResponse.getResponsecode()==200){
                    Toast.makeText(ApplyLeaveActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ApplyLeaveActivity.this,DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }else{
                    Toast.makeText(ApplyLeaveActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(ApplyLeaveActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataToSpinner() {
        String[] leaveType = {"Pre-request", "Planned", "Unplanned"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, leaveType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLeaveType.setAdapter(arrayAdapter);

        spinLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stLeaveType = spinLeaveType.getSelectedItem().toString();
                //Toast.makeText(ApplyLeaveActivity.this, "" + stleaveType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Data to spinner leave request
        String[] leaveTypeRequest = {"Sick leave", "Casual Leave", "Paid Leave", "Sectional/National Leave", "Maternity Leave", "Paternity Leave", "Transfer Leave", "Other"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, leaveTypeRequest);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLeaveRequest.setAdapter(arrayAdapter1);

        spinLeaveRequest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stLeaveRequest = spinLeaveRequest.getSelectedItem().toString();
              //  Toast.makeText(ApplyLeaveActivity.this, "" + stLeaveRequest, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean isValidated() {
        if (etStartDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Start date required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etEndDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "End date required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etLeaveReason.getText().toString().isEmpty()) {
            Toast.makeText(this, "Leave reason required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
