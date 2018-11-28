package com.linh.wiinav.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.tvComment.setText(comment.getContent());
        holder.tvComment.setOnLongClickListener((View.OnLongClickListener) v -> {
            //Display option menu

            PopupMenu popupMenu = new PopupMenu(context, holder.tvComment);
            popupMenu.inflate(R.menu.menu_comment_action);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.item_comment_edit:
                        Toast.makeText(context,"Editing",Toast.LENGTH_SHORT);
                        break;
                    case R.id.item_comment_delete:
                        Toast.makeText(context,"Deleting",Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
                return true;
            });
            return false;
        });
        holder.tvUsername.setText(comment.getCommentator().getUsername());
//        holder.tvCommentDate.setText(comment.getComme  mmntDate().toString());
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
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
        TextView tvComment;
        TextView tvEdit;
        TextView tvDelete;
        TextView tvCommentTime;

        ImageView imvCommentatorAvatar;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tv_comment);


            tvUsername = itemView.findViewById(R.id.tv_comment_user_name);
//            tvEdit = itemView.findViewById(R.id.tv_comment_edit);
//            tvDelete = itemView.findViewById(R.id.tv_comment_delete);
            imvCommentatorAvatar = itemView.findViewById(R.id.imv_commentator_avatar);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);

//            tvEdit.setOnClickListener(v -> {
//
//            });
//
//            tvDelete.setOnClickListener(v -> {
//
//            });
        }
    }
}
