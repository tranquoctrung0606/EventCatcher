package com.linh.wiinav.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        public UploadImageViewHolder(View itemView) {
            super(itemView);
            ivUploadImage = itemView.findViewById(R.id.iv_upload_image);
            ivUploadImage.setDrawingCacheEnabled(true);
            ivUploadImage.buildDrawingCache();
            ivRemove = itemView.findViewById(R.id.iv_report_image_remove);
        }
    }
}
