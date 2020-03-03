package com.praxello.leavemanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.model.viewstatus.ViewStatusData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRequestAdapter extends RecyclerView.Adapter<ViewRequestAdapter.ViewRequestViewHolder> {

    public ArrayList<ViewStatusData> viewStatusDataArrayList;
    public Context context;

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

        public ViewRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
