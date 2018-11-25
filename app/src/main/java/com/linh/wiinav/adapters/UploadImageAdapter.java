package com.linh.wiinav.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linh.wiinav.R;

import java.util.List;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.UploadImageViewHolder> {
    private List<String> imageNames;
    private Context context;

    public UploadImageAdapter(List<String> imageNames, Context context) {
        this.imageNames = imageNames;
        this.context = context;
    }

    @Override
    public UploadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_image_upload_item, parent, false);
        return new UploadImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UploadImageViewHolder holder, int position) {
        String imageName = imageNames.get(position);
        holder.tvImageName.setText(imageName);
        holder.pbUploading.setProgress(0);
        holder.ivRemove.setOnClickListener(l -> {
            imageNames.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    public class UploadImageViewHolder extends RecyclerView.ViewHolder {
        TextView tvImageName;
        ProgressBar pbUploading;
        ImageView ivRemove;

        public UploadImageViewHolder(View itemView) {
            super(itemView);
            tvImageName = itemView.findViewById(R.id.tv_report_image_name);
            pbUploading = itemView.findViewById(R.id.pb_report_image_loading);
            ivRemove = itemView.findViewById(R.id.iv_report_image_remove);
        }
    }
}
