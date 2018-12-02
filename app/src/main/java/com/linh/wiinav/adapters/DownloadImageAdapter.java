package com.linh.wiinav.adapters;

import android.app.Dialog;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linh.wiinav.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DownloadImageAdapter extends RecyclerView.Adapter<DownloadImageAdapter.DownloadImageViewHolder>{
    private static final String TAG = "DownloadImageAdapter";   
    private List<String> imageUrl = new ArrayList<>();
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private List<Uri> uris = new ArrayList<>();

    public DownloadImageAdapter() {
    }

    public DownloadImageAdapter(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        Log.e("image url size: ", imageUrl.size() + "");
        this.uris.clear();
        this.imageUrl = imageUrl;
        Log.e("image url size: ", imageUrl.size() + "");
        for (int i = 0; i < imageUrl.size(); i++) {
            Log.i("Log ", "vong lap nay co chay " + i);
            Log.i("size image url: ", imageUrl.size() + "");
            for (String url : imageUrl) {
                Log.e("url: ", url);
                mStorageReference
                        .child("images/" + url.substring(url.lastIndexOf("/")))
                        .getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            Log.i(TAG, "setImageUrl: " + uri);
                            uris.add(uri);
                            notifyItemInserted(uris.size());
                        }).addOnFailureListener(e -> Log.e(TAG, "onBindViewHolder: ", e));
            }
        }
    }

    @Override
    public DownloadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_image_download_item, parent, false);
        return new DownloadImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadImageViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        Picasso.get()
                .load(uris.get(position))
                .fit()
                .centerCrop()
                .into(holder.ivDownloadImage);
        holder.ivDownloadImage.setOnClickListener(v -> {
            Picasso.get()
                    .load(uris.get(position))
                    .fit()
                    .centerCrop()
                    .into(((PhotoView)holder.ivImageViewDialog.findViewById(R.id.iv_report_image_view)));
            holder.ivImageViewDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public class DownloadImageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivDownloadImage;
        Dialog ivImageViewDialog;

        public DownloadImageViewHolder(View itemView) {
            super(itemView);
            ivDownloadImage = itemView.findViewById(R.id.iv_report_download_image);
            ivImageViewDialog = new Dialog(itemView.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            ivImageViewDialog.setContentView(R.layout.dialog_image_view);
            ivImageViewDialog.findViewById(R.id.tv_report_exit_image_view).setOnClickListener(v -> ivImageViewDialog.dismiss());
        }
    }
}
