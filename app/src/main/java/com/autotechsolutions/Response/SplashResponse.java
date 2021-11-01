package com.autotechsolutions.Response;

import com.autotechsolutions.Model.BranchModel;
import com.autotechsolutions.Model.CityModel;
import com.autotechsolutions.Model.InsuranceCompanyModel;
import com.autotechsolutions.Model.MakeModel;
import com.autotechsolutions.Model.StateModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SplashResponse {
    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("about_us_text")
    @Expose
    private String about_us_text;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("sos_number")
    @Expose
    private String sos_number;

    @SerializedName("shareMessage")
    @Expose
    private String shareMessage;

    @SerializedName("arrMakeRecord")
    @Expose
    private ArrayList<MakeModel> arrMakeRecord;
    @SerializedName("arrStateRecord")
    @Expose
    private ArrayList<StateModel> arrStateRecord;
    @SerializedName("arrBranchRecord")
    @Expose
    private ArrayList<BranchModel> arrBranchRecord;
    @SerializedName("arrInsuranceCompanyRecord")
    @Expose
    private ArrayList<InsuranceCompanyModel> arrInsuranceCompanyRecord;

    public ArrayList<StateModel> getArrStateRecord() {
        return arrStateRecord;
    }

    public void setArrStateRecord(ArrayList<StateModel> arrStateRecord) {
        this.arrStateRecord = arrStateRecord;
    }

    public ArrayList<MakeModel> getArrMakeRecord() {
        return arrMakeRecord;
    }

    public void setArrMakeRecord(ArrayList<MakeModel> arrMakeRecord) {
        this.arrMakeRecord = arrMakeRecord;
    }


    public ArrayList<BranchModel> getArrBranchRecord() {
        return arrBranchRecord;
    }

    public void setArrBranchRecord(ArrayList<BranchModel> arrBranchRecord) {
        this.arrBranchRecord = arrBranchRecord;
    }

    public ArrayList<InsuranceCompanyModel> getArrInsuranceCompanyRecord() {
        return arrInsuranceCompanyRecord;
    }

    public void setArrInsuranceCompanyRecord(ArrayList<InsuranceCompanyModel> arrInsuranceCompanyRecord) {
        this.arrInsuranceCompanyRecord = arrInsuranceCompanyRecord;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAbout_us_text() {
        return about_us_text;
    }

    public void setAbout_us_text(String about_us_text) {
        this.about_us_text = about_us_text;
    }

    public String getSos_number() {
        return sos_number;
    }

    public void setSos_number(String sos_number) {
        this.sos_number = sos_number;
    }

    public String getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(String shareMessage) {
        this.shareMessage = shareMessage;
    }
}
