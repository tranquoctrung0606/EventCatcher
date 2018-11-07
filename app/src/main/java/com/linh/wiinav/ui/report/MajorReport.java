package com.linh.wiinav.ui.report;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class MajorReport extends ExpandableGroup<MinorReport>
{

    private int iconResId;

    public MajorReport(String title, List<MinorReport> items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MajorReport)) return false;

        MajorReport genre = (MajorReport) o;

        return getIconResId() == genre.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }
}
