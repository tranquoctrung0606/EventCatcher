package com.linh.wiinav.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linh.wiinav.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class DownloadImageAdapter extends RecyclerView.Adapter<DownloadImageAdapter.DownloadImageViewHolder>{
    private static final String TAG = "DownloadImageAdapter";   
    List<String> imageUrl;
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    public DownloadImageAdapter(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public DownloadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_image_download_item, parent, false);
        return new DownloadImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadImageViewHolder holder, int position) {
        String imageName = imageUrl.get(position);
        mStorageReference
                .child("images/" + imageName.substring(imageName.lastIndexOf("/")))
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(holder.ivDownloadImage);
                }).addOnFailureListener(e -> Log.e(TAG, "onBindViewHolder: ", e));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DownloadImageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivDownloadImage;

        public DownloadImageViewHolder(View itemView) {
            super(itemView);
            ivDownloadImage = itemView.findViewById(R.id.iv_report_download_image);
        }
    }
}
