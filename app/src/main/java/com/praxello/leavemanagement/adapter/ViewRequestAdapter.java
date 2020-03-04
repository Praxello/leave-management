package com.praxello.leavemanagement.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.leavemanagement.AllKeys;
import com.praxello.leavemanagement.CommonMethods;
import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.activity.ApplyLeaveActivity;
import com.praxello.leavemanagement.activity.DashBoardActivity;
import com.praxello.leavemanagement.activity.UpdateLeaveActivity;
import com.praxello.leavemanagement.activity.ViewRequestActivity;
import com.praxello.leavemanagement.model.CommonResponse;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusData;
import com.praxello.leavemanagement.services.ApiRequestHelper;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRequestAdapter extends RecyclerView.Adapter<ViewRequestAdapter.ViewRequestViewHolder> {

    public ArrayList<ViewStatusData> viewStatusDataArrayList;
    public Context context;
    public static String TAG="ViewRequestAdapter";

    public ViewRequestAdapter(ArrayList<ViewStatusData> viewStatusDataArrayList, Context context) {
        this.viewStatusDataArrayList = viewStatusDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_view_status_row,parent,false);
        return new ViewRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRequestViewHolder holder, int position) {

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

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("SmartQuiz")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                deleteLeave(viewStatusDataArrayList.get(position).getUser_id(),viewStatusDataArrayList.get(position).getLeave_id(),position);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //Toast.makeText(context, "Delete itemView " , Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent=new Intent(context, UpdateLeaveActivity.class);
                //intent.putExtra("data",quizDataArrayList.get(position).getQuestions());
                intent.putExtra("data",viewStatusDataArrayList.get(position));
                intent.putExtra("position",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });
    }

    private void deleteLeave(int user_id, int leave_id, int position) {
        Map<String,String> params=new HashMap<>();

        params.put("user_id", String.valueOf(user_id));
        params.put("leave_id", String.valueOf(leave_id));

        Log.e(TAG, "submitLeave: "+params );
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);
        ViewRequestActivity.smartQuiz.getApiRequestHelper().deleteLeaveRequest(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CommonResponse commonResponse=(CommonResponse) object;

                Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+commonResponse.getMessage());

                progress.dismiss();
                if(commonResponse.getResponsecode()==200){
                    Toast.makeText(context, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStatusDataArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,viewStatusDataArrayList.size());
                }else{
                    Toast.makeText(context, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                progress.dismiss();
                Toast.makeText(context, apiResponse, Toast.LENGTH_SHORT).show();
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
        @BindView(R.id.button_delete)
        TextView tvDelete;
        @BindView(R.id.button_edit)
        TextView tvEdit;

        public ViewRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
