package com.praxello.leavemanagement.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.activity.ViewRequestActivity;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusData;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusResponseAdmin;
import com.praxello.leavemanagement.services.ApiRequestHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminViewRequestAdapter  extends RecyclerView.Adapter<AdminViewRequestAdapter.ViewRequestViewHolder>{
    public ArrayList<ViewStatusData> viewStatusDataArrayList;
    public Context context;

    public AdminViewRequestAdapter(ArrayList<ViewStatusData> viewStatusDataArrayList, Context context) {
        this.viewStatusDataArrayList = viewStatusDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminViewRequestAdapter.ViewRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_admin_view_row,parent,false);
        return new AdminViewRequestAdapter.ViewRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewRequestAdapter.ViewRequestViewHolder holder, int position) {

        if(viewStatusDataArrayList.get(position).getLeave_status().equals("approved")){
            holder.tvStatus.setTextColor(Color.parseColor("#009688"));
        }else{
            holder.tvStatus.setTextColor(Color.parseColor("#ff0000"));
        }

        String startDate = viewStatusDataArrayList.get(position).getStart_date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        // Log.e(TAG, "simple date format: "+spf.toString());
        Date newDate = null;
        try {
            newDate = spf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat(" EEE, dd MMM yy");
        startDate = spf.format(newDate);
        holder.tvStartDate.setText(startDate);

        String endDate = viewStatusDataArrayList.get(position).getEnd_date();
        spf = new SimpleDateFormat("yyyy-MM-dd");
        // Log.e(TAG, "simple date format: "+spf.toString());
        newDate = null;
        try {
            newDate = spf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat(" EEE, dd MMM yy");
        endDate = spf.format(newDate);
        holder.tvEndDate.setText(endDate);
        holder.tvLeaveType.setText(viewStatusDataArrayList.get(position).getLeave_type());
        holder.tvDetails.setText(viewStatusDataArrayList.get(position).getLeave_reason());
        holder.tvStatus.setText(viewStatusDataArrayList.get(position).getLeave_status());
        holder.tvApprovedBy.setText(viewStatusDataArrayList.get(position).getAfname()+" "+viewStatusDataArrayList.get(position).getAlname());
        holder.tvUserName.setText(viewStatusDataArrayList.get(position).getFirstName()+" "+viewStatusDataArrayList.get(position).getLastName());

        holder.llApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(viewStatusDataArrayList.get(position).getLeave_id(),viewStatusDataArrayList.get(position).getUser_id(),"approved",position,holder);
            }
        });

        holder.llReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(viewStatusDataArrayList.get(position).getLeave_id(),viewStatusDataArrayList.get(position).getUser_id(),"reject",position,holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewStatusDataArrayList.size();
    }

    public class ViewRequestViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_start_date)
        TextView tvStartDate;
        @BindView(R.id.tv_end_date)
        TextView tvEndDate;
        @BindView(R.id.tv_leave_type)
        TextView tvLeaveType;
        @BindView(R.id.tv_details)
        TextView tvDetails;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_approval_by)
        TextView tvApprovedBy;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.ll_approved)
        LinearLayout llApproved;
        @BindView(R.id.ll_reject)
        LinearLayout llReject;

        public ViewRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void updateStatus(int leaveId,int userId,String status,int position,@NonNull AdminViewRequestAdapter.ViewRequestViewHolder holder){
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);

        Map<String,String> params=new HashMap<>();
        params.put("user_id", String.valueOf(userId));
        params.put("leave_id", String.valueOf(leaveId));
        params.put("leave_status",status);
        params.put("approvedBy", CommonMethods.getPrefrence(context, AllKeys.USER_ID));

        //Log.e(TAG, "submitLeave: "+params );
        ViewRequestActivity.smartQuiz.getApiRequestHelper().updateStatus(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                ViewStatusResponseAdmin viewStatusResponseAdmin=(ViewStatusResponseAdmin) object;

              //  Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
               // Log.e(TAG, "onSuccess: "+commonResponse.getMessage());
                    progress.dismiss();
                if(viewStatusResponseAdmin.getResponsecode()==200){
                    Toast.makeText(context, viewStatusResponseAdmin.getMessage(), Toast.LENGTH_SHORT).show();
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

                    viewStatusDataArrayList.set(position,viewStatusData);
                    notifyItemChanged(position);

                    if(viewStatusDataArrayList.get(position).getLeave_status().equals("approved")){
                        holder.tvStatus.setTextColor(Color.parseColor("#009688"));
                    }else{
                        holder.tvStatus.setTextColor(Color.parseColor("#ff0000"));
                    }
                }else{
                    Toast.makeText(context, viewStatusResponseAdmin.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                progress.dismiss();
                Toast.makeText(context, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
