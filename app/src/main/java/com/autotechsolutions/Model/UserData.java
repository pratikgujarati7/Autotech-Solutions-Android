package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("isVerified")
    @Expose
    private String isVerified;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("areaID")
    @Expose
    private String areaID;
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("stateID")
    @Expose
    private String stateID;
    @SerializedName("stateName")
    @Expose
    private String stateName;

    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("cityID")
    @Expose
    private String cityID;
    @SerializedName("referralCode")
    @Expose
    private String referralCode;

    public String getReferralMessage() {
        return referralMessage;
    }

    public void setReferralMessage(String referralMessage) {
        this.referralMessage = referralMessage;
    }

    @SerializedName("referralMessage")
    @Expose
    private String referralMessage;

    @SerializedName("isUserAlreadyRegistered")
    @Expose
    private Integer isUserAlreadyRegistered;

    public Integer getIsUserAlreadyRegistered() {
        return isUserAlreadyRegistered;
    }

    public void setIsUserAlreadyRegistered(Integer isUserAlreadyRegistered) {
        this.isUserAlreadyRegistered = isUserAlreadyRegistered;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
