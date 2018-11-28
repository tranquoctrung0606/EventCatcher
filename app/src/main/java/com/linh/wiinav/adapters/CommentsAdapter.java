package com.linh.wiinav.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linh.wiinav.R;
import com.linh.wiinav.helpers.Utils;
import com.linh.wiinav.models.AskHelp;
import com.linh.wiinav.models.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{
    private List<Comment> comments;
    private Context context;
    private AskHelp currentAskHelp;
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    public CommentsAdapter(List<Comment> comments, Context context, AskHelp currentAskHelp) {
        this.comments = comments;
        this.context = context;
        this.currentAskHelp = currentAskHelp;
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvContent.setText(comment.getContent());
        holder.tvContent.setOnLongClickListener(v -> {
            //Display option menu
            PopupMenu popupMenu = new PopupMenu(context, holder.tvContent);
            popupMenu.inflate(R.menu.menu_comment_action);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.item_comment_edit:
                        Toast.makeText(context,"Editing",Toast.LENGTH_SHORT);
                        editComment(comment, position);
                        break;
                    case R.id.item_comment_delete:
                        Toast.makeText(context,"Deleting",Toast.LENGTH_SHORT);
                        removeComment(comment, position);
                        break;
                    default:
                        break;
                }
                return true;
            });
            popupMenu.show();
            return false;
        });
        holder.tvUsername.setText(comment.getCommentator().getUsername());
        //Caculate comment time
        holder.tvCommentTime.setText(Utils.getTiming(comment.getCommentDate()));
    }

    private void editComment(Comment comment, int position) {
        Map<String, Object> updatedComment = new HashMap<>();
        comments.set(position, comment);
        updatedComment.put(String.valueOf(position), comment);
        mDatabaseReference.child("askHelps").child(currentAskHelp.getId()).child("comments")
                .updateChildren(updatedComment).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        comments.add(comment);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Delete successful.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Can not delete this comment. Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                });
        mDatabaseReference.child("comments").child(comment.getCommentId()).removeValue();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentsViewHolder(itemView);
    }

    private void removeComment(Comment comment, int position) {
        mDatabaseReference.child("askHelps").child(currentAskHelp.getId()).child("comments").child(String.valueOf(position))
                .removeValue().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        comments.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Delete successful.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Can not delete this comment. Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                });
        mDatabaseReference.child("comments").child(comment.getCommentId()).removeValue();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView tvUsername;
        TextView tvContent;
        TextView tvCommentTime;
        ImageView imvCommentatorAvatar;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvUsername = itemView.findViewById(R.id.tv_comment_user_name);
            imvCommentatorAvatar = itemView.findViewById(R.id.imv_commentator_avatar);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
        }
    }
}
