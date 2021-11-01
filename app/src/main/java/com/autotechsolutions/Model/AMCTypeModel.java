package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AMCTypeModel {
    @SerializedName("amcID")
    @Expose
    private String amcID;
    @SerializedName("amcTitle")
    @Expose
    private String amcTitle;
    @SerializedName("isPurchased")
    @Expose
    private String isPurchased;
    @SerializedName("amcUserID")
    @Expose
    private String amcUserID;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("amcDescription")
    @Expose
    private String amcDescription;
    @SerializedName("amcType")
    @Expose
    private String amcType;
    @SerializedName("amcDetails")
    @Expose
    private ArrayList<AMCModel> amcDetails;

    public String getAmcType() {
        return amcType;
    }

    public void setAmcType(String amcType) {
        this.amcType = amcType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmcDescription() {
        return amcDescription;
    }

    public void setAmcDescription(String amcDescription) {
        this.amcDescription = amcDescription;
    }

    public String getAmcID() {
        return amcID;
    }

    public void setAmcID(String amcID) {
        this.amcID = amcID;
    }

    public String getAmcTitle() {
        return amcTitle;
    }

    public void setAmcTitle(String amcTitle) {
        this.amcTitle = amcTitle;
    }

    public String getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(String isPurchased) {
        this.isPurchased = isPurchased;
    }

    public String getAmcUserID() {
        return amcUserID;
    }

    public void setAmcUserID(String amcUserID) {
        this.amcUserID = amcUserID;
    }

    public ArrayList<AMCModel> getAmcDetails() {
        return amcDetails;
    }

    public void setAmcDetails(ArrayList<AMCModel> amcDetails) {
        this.amcDetails = amcDetails;
    }
}
