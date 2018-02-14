package com.training.android.undivided.NavigationMode;

/**
 * Created by Dyste on 2/13/2018.
 */

public class TowingServicesModel {

    private String Name;
    private String Address;
    private String latlng;
    private String ContactNumber;



    public TowingServicesModel() {
    }

    public TowingServicesModel(String name, String address, String latlng, String contactNumber) {
        Name = name;
        Address = address;
        this.latlng = latlng;
        ContactNumber = contactNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
