package com.autotechsolutions.Model;

public class AddCarModel {

    String strCompanyName,strCarMode,strRegisterNumber,strId;

    public String getStrCompanyName() {
        return strCompanyName;
    }

    public void setStrCompanyName(String strCompanyName) {
        this.strCompanyName = strCompanyName;
    }

    public String getStrCarMode() {
        return strCarMode;
    }

    public void setStrCarMode(String strCarMode) {
        this.strCarMode = strCarMode;
    }

    public String getStrRegisterNumber() {
        return strRegisterNumber;
    }

    public void setStrRegisterNumber(String strRegisterNumber) {
        this.strRegisterNumber = strRegisterNumber;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public AddCarModel(String strCompanyName, String strCarMode, String strRegisterNumber, String strId)
    {
        this.strCompanyName = strCompanyName;
        this.strCarMode = strCarMode;
        this.strRegisterNumber = strRegisterNumber;
        this.strId = strId;
    }
}
