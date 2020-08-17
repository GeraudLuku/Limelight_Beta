package com.krystofrapp.limelightbeauty;


public class ProfileDetails {

    private String businessName;
    private String businessNumber;
    private String businessAddress;
    private String businessNickname;

    public ProfileDetails() {
        //Empty constructor needed here.
    }

    public ProfileDetails(String businessName, String businessNumber, String businessAddress, String businessNickname) {
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.businessAddress = businessAddress;
        this.businessNickname = businessNickname;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessNickname() {
        return businessNickname;
    }

    public void setBusinessNickname(String businessNickname) {
        this.businessNickname = businessNickname;
    }
}

/*

 */
