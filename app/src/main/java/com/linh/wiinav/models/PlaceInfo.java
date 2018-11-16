package com.linh.wiinav.models;

import android.net.Uri;
import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo
{
    private String name, address, attribution, phoneNumber, id;
    private Uri websiteUri;
    private float rating;
    private LatLng latLng;

    public PlaceInfo(final String name, final String address, final String attribution, final String phoneNumber, final String id, final Uri websiteUri, final float rating, final LatLng latLng)
    {
        this.name = name;
        this.address = address;
        this.attribution = attribution;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.rating = rating;
        this.latLng = latLng;
    }

    public PlaceInfo()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(final String address)
    {
        this.address = address;
    }

    public String getAttribution()
    {
        return attribution;
    }

    public void setAttribution(final String attribution)
    {
        this.attribution = attribution;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public Uri getWebsiteUri()
    {
        return websiteUri;
    }

    public void setWebsiteUri(final Uri websiteUri)
    {
        this.websiteUri = websiteUri;
    }

    public float getRating()
    {
        return rating;
    }

    public void setRating(final float rating)
    {
        this.rating = rating;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(final LatLng latLng)
    {
        this.latLng = latLng;
    }

    @Override
    public String toString()
    {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", attribution='" + attribution + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", rating=" + rating +
                ", latLng=" + latLng +
                '}';
    }
}
