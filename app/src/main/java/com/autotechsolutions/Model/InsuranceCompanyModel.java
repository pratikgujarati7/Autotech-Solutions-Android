package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsuranceCompanyModel {
    @SerializedName("insuranceCompanyID")
    @Expose
    private String insuranceCompanyID;
    @SerializedName("companyName")
    @Expose
    private String companyName;

    public String getInsuranceCompanyID() {
        return insuranceCompanyID;
    }

    public void setInsuranceCompanyID(String insuranceCompanyID) {
        this.insuranceCompanyID = insuranceCompanyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
