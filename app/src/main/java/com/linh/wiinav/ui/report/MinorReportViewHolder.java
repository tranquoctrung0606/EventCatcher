package com.linh.wiinav.ui.report;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.linh.wiinav.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class MinorReportViewHolder extends ChildViewHolder
{
    public TextView getChildTextView()
    {
        return childTextView;
    }

    private TextView childTextView;
    private ImageView icon;

    public MinorReportViewHolder(final View itemView)
    {
        super(itemView);
        childTextView = (TextView) itemView.findViewById(R.id.list_item_minorreport_name);
    }

    public void setMinorReportName(String name) {
        childTextView.setText(name);
    }

}
