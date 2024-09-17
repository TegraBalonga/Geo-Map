package com.example.geomap;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Identity;
import java.util.ArrayList;

public class MyClass {

    public String username, mapType, landmarkType, landmark, placeId, website;

    public ArrayList<MarkerOptions> arrDelaysMakerOp;
    public ArrayList<String> arrDelaysType ;
    public ArrayList<String> arrDelaysTime ;


    private static final MyClass ourInstance = new MyClass();

    public static MyClass getInstance() {
        return ourInstance;
    }


   public void getFavLandmark(String landmark, String placeId, String website) {
        this.landmark = landmark;
        this.placeId = placeId;
        this.website = website;

    }
    public void getFavLandmark(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getLandmarkType() {
        return landmarkType;
    }

    public void setLandmarkType(String landmarkType) {
        this.landmarkType = landmarkType;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ArrayList<MarkerOptions> getArrDelaysMakerOp() {
        return arrDelaysMakerOp;
    }

    public void setArrDelaysMakerOp(ArrayList<MarkerOptions> arrDelaysMakerOp) {
        this.arrDelaysMakerOp = arrDelaysMakerOp;
    }

    public ArrayList<String> getArrDelaysType() {
        return arrDelaysType;
    }

    public void setArrDelaysType(ArrayList<String> arrDelaysType) {
        this.arrDelaysType = arrDelaysType;
    }

    public ArrayList<String> getArrDelaysTime() {
        return arrDelaysTime;
    }

    public void setArrDelaysTime(ArrayList<String> arrDelaysTime) {
        this.arrDelaysTime = arrDelaysTime;
    }



    //////////////////////////////////////////PLACE INFO CLASS DATA////////////////////////////////

    public String name;
    public String address;
    public String phoneNumber;
    public String id;
    public ArrayList<String> favId;
    public Uri websiteUri;
    public LatLng latLng;
    public Double rating;
    public String attributions;


    public void PlaceInfo() {

    }
    //constructor
    public void PlaceInfo(String name, String address, String phoneNumber,
                     String id, Uri websiteUri, LatLng latLng,
                     Double rating) {

        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.rating = rating;

    }

    public ArrayList<String> getFavId() {
        return favId;
    }

    public void setFavId(ArrayList<String> favId) {
        this.favId = favId;
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
