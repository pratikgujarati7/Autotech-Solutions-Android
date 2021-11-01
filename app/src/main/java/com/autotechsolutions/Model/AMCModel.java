package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AMCModel {
    @SerializedName("amcID")
    @Expose
    private String amcID;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("amcCoupanID")
    @Expose
    private String amcCoupanID;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subTitle")
    @Expose
    private String subTitle;
    @SerializedName("labourDetails")
    @Expose
    private String labourDetails;
    @SerializedName("partsDetails")
    @Expose
    private String partsDetails;
    @SerializedName("tnc")
    @Expose
    private String tnc;

    public String getAmcID() {
        return amcID;
    }

    public void setAmcID(String amcID) {
        this.amcID = amcID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmcCoupanID() {
        return amcCoupanID;
    }

    public void setAmcCoupanID(String amcCoupanID) {
        this.amcCoupanID = amcCoupanID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLabourDetails() {
        return labourDetails;
    }

    public void setLabourDetails(String labourDetails) {
        this.labourDetails = labourDetails;
    }

    public String getPartsDetails() {
        return partsDetails;
    }

    public void setPartsDetails(String partsDetails) {
        this.partsDetails = partsDetails;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }
}
