package com.praxello.leavemanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
import com.praxello.leavemanagement.model.viewstatus.ViewStatusData;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponseAdmin;
import com.praxello.leavemanagement.services.ApiRequestHelper;
import com.praxello.leavemanagement.services.SmartQuiz;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateLeaveActivity extends AppCompatActivity implements View.OnClickListener{

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
    ViewStatusData viewStatusData;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_leave);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        if(getIntent().getParcelableExtra("data")!=null){
            viewStatusData=getIntent().getParcelableExtra("data");
            position=getIntent().getIntExtra("position",0);
        }

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
        toolbar.setTitle("Update Leave");
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
                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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

                                etEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

                break;

            case R.id.btn_submit:
                if(isValidated()){
                   updateLeave();
                }
                break;
        }
    }

    private void updateLeave(){
        final ProgressDialog progress = new ProgressDialog(UpdateLeaveActivity.this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);

        Map<String,String> params=new HashMap<>();
        params.put("user_id", String.valueOf(viewStatusData.getUser_id()));
        params.put("leave_id", String.valueOf(viewStatusData.getLeave_id()));
        params.put("leave_request",stLeaveRequest);
        params.put("leave_type",stLeaveType);
        params.put("start_date",etStartDate.getText().toString());
        params.put("end_date",etEndDate.getText().toString());
        params.put("leave_status",viewStatusData.getLeave_status());
        params.put("leave_reason",etLeaveReason.getText().toString());

        //Log.e(TAG, "submitLeave: "+params );
        smartQuiz.getApiRequestHelper().updateLeave(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                ViewStatusResponseAdmin viewStatusResponseAdmin=(ViewStatusResponseAdmin) object;

                //  Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
                // Log.e(TAG, "onSuccess: "+commonResponse.getMessage());
                progress.dismiss();
                if(viewStatusResponseAdmin.getResponsecode()==200){
                    Toast.makeText(UpdateLeaveActivity.this, viewStatusResponseAdmin.getMessage(), Toast.LENGTH_SHORT).show();
                    ViewStatusData viewStatusData=new ViewStatusData(
                            viewStatusResponseAdmin.getData().getLeave_id(),
                            viewStatusResponseAdmin.getData().getUser_id(),
                            viewStatusResponseAdmin.getData().getLeave_request(),
                            viewStatusResponseAdmin.getData().getLeave_type(),
                            viewStatusResponseAdmin.getData().getLeave_reason(),
                            viewStatusResponseAdmin.getData().getStart_date(),
                            viewStatusResponseAdmin.getData().getEnd_date(),
                            viewStatusResponseAdmin.getData().getLeave_status(),
                            viewStatusResponseAdmin.getData().getAfname(),
                            viewStatusResponseAdmin.getData().getAlname(),
                            viewStatusResponseAdmin.getData().getFirstName(),
                            viewStatusResponseAdmin.getData().getLastName());

                            ViewRequestActivity.viewStatusDataArrayList.set(position,viewStatusData);
                            ViewRequestActivity.viewRequestAdapter.notifyItemChanged(position);
                            finish();
                            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_translate);
                }else{
                    Toast.makeText(UpdateLeaveActivity.this, viewStatusResponseAdmin.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                progress.dismiss();
                Toast.makeText(UpdateLeaveActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
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

        etStartDate.setText(viewStatusData.getStart_date());
        etEndDate.setText(viewStatusData.getEnd_date());
        etLeaveReason.setText(viewStatusData.getLeave_reason());
        for(int i=0;i<leaveType.length;i++){
            if(viewStatusData.getLeave_type().equals(leaveType[i])){
                spinLeaveType.setSelection(i);
                break;
            }
        }

        for(int i=0;i<leaveTypeRequest.length;i++){
            if(viewStatusData.getLeave_request().equals(leaveTypeRequest[i])){
                spinLeaveRequest.setSelection(i);
                break;
            }
        }

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

