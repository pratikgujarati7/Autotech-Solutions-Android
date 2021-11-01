package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAddressBookListModel {

    @SerializedName("userAddressBookID")
    @Expose
    private String userAddressBookID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("address")
    @Expose
    private String address;


    public String getUserAddressBookID() {
        return userAddressBookID;
    }

    public void setUserAddressBookID(String userAddressBookID) {
        this.userAddressBookID = userAddressBookID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
