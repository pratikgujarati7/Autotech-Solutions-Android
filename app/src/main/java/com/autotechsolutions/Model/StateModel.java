package com.autotechsolutions.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StateModel {

    @SerializedName("stateID")
    @Expose
    private String stateID;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("arrAreaRecord")
    @Expose
    private ArrayList<CityModel> arrAreaRecord;


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

    public ArrayList<CityModel> getArrAreaRecord() {
        return arrAreaRecord;
    }

    public void setArrAreaRecord(ArrayList<CityModel> arrAreaRecord) {
        this.arrAreaRecord = arrAreaRecord;
    }
}
