package com.linh.wiinav.ui.report;

import android.os.Parcel;
import android.os.Parcelable;

public class MinorReport implements Parcelable
{
    private String name;



    private int iconResId;

    public MinorReport(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    protected MinorReport(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public int getIconResId() { return iconResId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinorReport)) return false;

        MinorReport artist = (MinorReport) o;

        return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result;
        return result;
    }

    public static final Creator<MinorReport> CREATOR = new Creator<MinorReport>()
    {
        @Override
        public MinorReport createFromParcel(Parcel in)
        {
            return new MinorReport(in);
        }
        @Override
        public MinorReport[] newArray(int size)
        {
            return new MinorReport[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags)
    {
        dest.writeString(name);
    }
}
