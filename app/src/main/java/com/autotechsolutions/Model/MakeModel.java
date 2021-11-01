package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MakeModel {
    @SerializedName("makeID")
    @Expose
    private String makeID;
    @SerializedName("makeName")
    @Expose
    private String makeName;
    @SerializedName("arrModelRecord")
    @Expose
    private ArrayList<ModelMakeModel> arrModelRecord;

    public ArrayList<ModelMakeModel> getArrModelRecord() {
        return arrModelRecord;
    }

    public void setArrModelRecord(ArrayList<ModelMakeModel> arrModelRecord) {
        this.arrModelRecord = arrModelRecord;
    }

    public String getMakeID() {
        return makeID;
    }

    public void setMakeID(String makeID) {
        this.makeID = makeID;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }
}
