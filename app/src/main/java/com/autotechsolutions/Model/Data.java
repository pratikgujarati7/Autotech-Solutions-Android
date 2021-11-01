package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {

    @SerializedName("userDetails")
    @Expose
    private UserData userDetails;
    @SerializedName("carDetails")
    @Expose
    private ArrayList<carsModel> carDetails;
    @SerializedName("productsList")
    @Expose
    private ArrayList<CarCareModel> productsList;
    @SerializedName("userAllNotificationList")
    @Expose
    private ArrayList<NotificationModel> userAllNotificationList;
    @SerializedName("arrAMC")
    @Expose
    private ArrayList<AMCTypeModel> arrAMC;


    @SerializedName("userAddressBookList")
    @Expose
    private ArrayList<UserAddressBookListModel> userAddressBookList;

    @SerializedName("IsPurchased")
    @Expose
    private String IsPurchased;
    @SerializedName("amcID")
    @Expose
    private String amcID;
    @SerializedName("amcUserID")
    @Expose
    private String amcUserID;

    public ArrayList<AMCTypeModel> getArrAMC() {
        return arrAMC;
    }

    public void setArrAMC(ArrayList<AMCTypeModel> arrAMC) {
        this.arrAMC = arrAMC;
    }

    public ArrayList<UserAddressBookListModel> getUserAddressBookList() {
        return userAddressBookList;
    }

    public void setUserAddressBookList(ArrayList<UserAddressBookListModel> userAddressBookList) {
        this.userAddressBookList = userAddressBookList;
    }

    public String getAmcID() {
        return amcID;
    }

    public void setAmcID(String amcID) {
        this.amcID = amcID;
    }


    public String getIsPurchased() {
        return IsPurchased;
    }

    public void setIsPurchased(String isPurchased) {
        IsPurchased = isPurchased;
    }

    public String getAmcUserID() {
        return amcUserID;
    }

    public void setAmcUserID(String amcUserID) {
        this.amcUserID = amcUserID;
    }

    public ArrayList<NotificationModel> getUserAllNotificationList() {
        return userAllNotificationList;
    }

    public void setUserAllNotificationList(ArrayList<NotificationModel> userAllNotificationList) {
        this.userAllNotificationList = userAllNotificationList;
    }

    public ArrayList<CarCareModel> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<CarCareModel> productsList) {
        this.productsList = productsList;
    }

    public ArrayList<carsModel> getCarDetails() {
        return carDetails;
    }

    public void setCarDetails(ArrayList<carsModel> carDetails) {
        this.carDetails = carDetails;
    }

    public UserData getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserData userDetails) {
        this.userDetails = userDetails;
    }
}
