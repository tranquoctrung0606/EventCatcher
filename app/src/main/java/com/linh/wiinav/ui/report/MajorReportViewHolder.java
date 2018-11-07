package com.linh.wiinav.ui.report;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.linh.wiinav.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MajorReportViewHolder extends GroupViewHolder
{
    private TextView majorrReportName;
    private ImageView arrow;
    private ImageView icon;

    public MajorReportViewHolder(View itemView) {
        super(itemView);
        majorrReportName = (TextView) itemView.findViewById(R.id.list_item_major_name);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_major_arrow);
        icon = (ImageView) itemView.findViewById(R.id.list_item_major_icon);
    }

    public void setMajorReportTitle(ExpandableGroup majorReportTitle) {
        majorrReportName.setText(majorReportTitle.getTitle());
        icon.setBackgroundResource(((MajorReport) majorReportTitle).getIconResId());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

}
