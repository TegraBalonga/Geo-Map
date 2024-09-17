package com.example.geomap;

import android.app.Application;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo extends Application {

    //initializing the variables
    public String name;
    public String address;
    public String phoneNumber;
    public String id;
    public Uri websiteUri;
    public LatLng latLng;
    public Double rating;
    public String attributions;


    public PlaceInfo() {

    }
    //constructor
    public PlaceInfo(String name, String address, String phoneNumber,
                     String id, Uri websiteUri, LatLng latLng,
                     Double rating) {

        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.rating = rating;
       // this.attributions = attributions;
    }

    //get and set
    public String getName() {
        return name;
    }

    public void setName(String name1) {
        name = name1;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address1) {
        address = address1;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber1) {
        phoneNumber = phoneNumber1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id1) {
        id = id1;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUr1) {
        websiteUri = websiteUr1;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLn1) { latLng = latLn1; }

    public double getRating() {
        return rating;
    }

    public void setRating(Double rating1) {
        rating = rating1;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attribution1) {
        attributions = attribution1;
    }

    @Override
    public   String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}
