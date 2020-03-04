package com.praxello.leavemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.activity.AllInOneSocialActivity;
import com.praxello.leavemanagement.model.SocialMediaData;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SocialMediaAdapter extends RecyclerView.Adapter<SocialMediaAdapter.SocialMediaViewHolder> {

    public Context context;
    public ArrayList<SocialMediaData> socialMediaDataArrayList;

    public SocialMediaAdapter(Context context, ArrayList<SocialMediaData> socialMediaDataArrayList) {
        this.context = context;
        this.socialMediaDataArrayList = socialMediaDataArrayList;
    }

    @NonNull
    @Override
    public SocialMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_social_media_row,parent,false);
        return new SocialMediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialMediaViewHolder holder, int position) {
        holder.tvSocialName.setText(socialMediaDataArrayList.get(position).getSocialMediaName());
        //Glide.with(context).load(socialMediaDataArrayList.get(position).getSocialMediaImage()).into(holder.ivSocialImage);
        holder.ivSocialImage.setBackgroundDrawable(context.getResources().getDrawable(socialMediaDataArrayList.get(position).getSocialMediaImage()));

        holder.llSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((AllInOneSocialActivity) context).adapterClickEvents(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return socialMediaDataArrayList.size();
    }

    public class SocialMediaViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ll_socialmedia)
        LinearLayout llSocialMedia;
        @BindView(R.id.tv_socialname)
        TextView tvSocialName;
        @BindView(R.id.iv_socialimage)
        ImageView ivSocialImage;

        public SocialMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
