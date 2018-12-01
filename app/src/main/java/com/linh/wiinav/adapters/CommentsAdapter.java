package com.linh.wiinav.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvUsername.setText(comment.getCommentator().getUsername());
        //Caculate comment time
        holder.tvCommentTime.setText(Utils.getTiming(comment.getCommentDate()));
        holder.etContent.setText(comment.getContent());
        holder.etContent.setOnLongClickListener(v -> {
            //Display option menu
            PopupMenu popupMenu = new PopupMenu(context, holder.etContent);
            //Get current user
            String currentUserId = FirebaseAuth.getInstance().getUid();
            assert currentUserId != null;
            if (currentUserId.equals(currentAskHelp.getPoster().getId()))
                popupMenu.inflate(R.menu.menu_comment_action_for_poster);
            if (currentUserId.equals(comment.getCommentator().getId()))
                popupMenu.inflate(R.menu.menu_comment_action_for_commentator);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.item_comment_hide:
                        hideComment(comment, position);
                        break;
                    case R.id.item_comment_edit:
                        holder.etContent.setFocusable(true);
                        holder.etContent.setCursorVisible(true);
                        holder.etContent.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_send,0);
                        holder.etContent.setOnTouchListener((v1, event) -> {
                            final int DRAWABLE_RIGHT = 2;
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getRawX() >= (holder.etContent.getRight() - holder.etContent.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                    editComment(comment, position);
                                    return true;
                                }
                            }
                            return false;
                        });
                    case R.id.item_comment_delete:
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
    }

    private void hideComment(Comment comment, int position) {
        Toast.makeText(context,"This feature is not available",Toast.LENGTH_SHORT).show();
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
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    mDatabaseReference.child("askHelps")
                                        .child(currentAskHelp.getId())
                                        .child("comments")
                                        .child(String.valueOf(position)).removeValue()
                                        .addOnCompleteListener(task -> {
                                            if (task.isComplete()) {
                                                comments.remove(position);
                                                notifyItemRemoved(position);
                                                Toast.makeText(context, "Delete successful.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Can not delete this comment. Please check your connection.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    mDatabaseReference.child("comments").child(comment.getCommentId()).removeValue();
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    dialog.dismiss();
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to delete this comment?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView tvUsername;
        EditText etContent;
        TextView tvCommentTime;
        ImageView imvCommentatorAvatar;

        CommentsViewHolder(View itemView) {
            super(itemView);
            etContent = itemView.findViewById(R.id.et_content);
            etContent.setFocusable(false);
            etContent.setCursorVisible(false);
            tvUsername = itemView.findViewById(R.id.tv_comment_user_name);
            imvCommentatorAvatar = itemView.findViewById(R.id.imv_commentator_avatar);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
        }
    }
}
