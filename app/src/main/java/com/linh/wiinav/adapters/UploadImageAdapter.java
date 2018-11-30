package com.linh.wiinav.adapters;

import android.app.Dialog;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.FirebaseStorage;
import com.linh.wiinav.R;
import com.linh.wiinav.ui.ReportDetailActivity;

import java.util.List;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.UploadImageViewHolder> {
    private static final String TAG = "UploadImageAdapter";
    private List<String> imageNames;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public UploadImageAdapter(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    @Override
    public UploadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_image_upload_item, parent, false);
        return new UploadImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UploadImageViewHolder holder, int position) {
        String imageName = imageNames.get(position);
        holder.ivUploadImage.setImageURI(Uri.parse(imageName));
        holder.ivUploadImage.setOnClickListener(v -> {
            holder.ivImageView.setImageURI(Uri.parse(imageName));
            holder.dialogImageView.show();
        });
        holder.ivRemove.setOnClickListener(l -> {
            firebaseStorage.getReference()
                    .child("images/"+imageNames.get(position).substring(imageNames.get(position)
                            .lastIndexOf("/")))
                    .delete().addOnCompleteListener(task -> {
                        imageNames.remove(position);
                        notifyDataSetChanged();
                        ReportDetailActivity.countImage--;
                    });
        });
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    public class UploadImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUploadImage;
        ImageView ivRemove;
        PhotoView ivImageView;
        TextView tvExit;
        Dialog dialogImageView;

        public UploadImageViewHolder(View itemView) {
            super(itemView);
            ivUploadImage = itemView.findViewById(R.id.iv_upload_image);
            ivUploadImage.setDrawingCacheEnabled(true);
            ivUploadImage.buildDrawingCache();
            ivRemove = itemView.findViewById(R.id.iv_report_image_remove);

            //
            dialogImageView = new Dialog(itemView.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialogImageView.setContentView(R.layout.dialog_image_view);
            ivImageView = dialogImageView.findViewById(R.id.iv_report_image_view);
            tvExit = dialogImageView.findViewById(R.id.tv_report_exit_image_view);
            tvExit.setOnClickListener(v -> {
                dialogImageView.dismiss();
            });
        }
    }
}
