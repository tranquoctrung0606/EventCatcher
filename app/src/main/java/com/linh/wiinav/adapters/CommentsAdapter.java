package com.linh.wiinav.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linh.wiinav.R;
import com.linh.wiinav.models.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{
    private List<Comment> comments;
    private Context context;

    public CommentsAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvContent.setText(comment.getContent());
        holder.tvUsername.setText(comment.getCommentator().getUsername());
        holder.tvCommentDate.setText(comment.getCommentDate().toString());
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_comment_item, parent, false);
        return new CommentsViewHolder(itemView);
    }

    private void removeComment(int position) {
        comments.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView tvUsername;
        TextView tvContent;
        TextView tvEdit;
        TextView tvDelete;
        TextView tvCommentDate;

        ImageView ivUserImage;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_comment_content);
            tvUsername = itemView.findViewById(R.id.tv_comment_user_name);
            tvEdit = itemView.findViewById(R.id.tv_comment_edit);
            tvDelete = itemView.findViewById(R.id.tv_comment_delete);
            ivUserImage = itemView.findViewById(R.id.iv_comment_user_image);
            tvCommentDate = itemView.findViewById(R.id.tv_comment_date);

            tvEdit.setOnClickListener(v -> {

            });

            tvDelete.setOnClickListener(v -> {

            });
        }
    }
}
