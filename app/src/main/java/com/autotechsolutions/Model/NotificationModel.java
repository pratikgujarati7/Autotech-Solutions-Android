package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("notificationID")
    @Expose
    private String notificationID;
    @SerializedName("notificationTitle")
    @Expose
    private String notificationTitle;
    @SerializedName("notificationText")
    @Expose
    private String notificationText;
    @SerializedName("notificationDate")
    @Expose
    private String notificationDate;

    public String getNotificationID() {
        return notificationID;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public String getNotificationDate() {
        return notificationDate;
    }
}
