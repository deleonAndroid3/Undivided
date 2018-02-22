package com.training.android.undivided.Models;

/**
 * Created by Dyste on 2/18/2018.
 */

public class EmergencyContactsModel {

    private String Name;
    private String Address;
    private String EcontactNumber;
    private String Etype;

    public EmergencyContactsModel() {
    }

    public EmergencyContactsModel(String name, String address, String econtactNumber, String etype) {
        Name = name;
        Address = address;
        EcontactNumber = econtactNumber;
        Etype = etype;
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

    public String getEcontactNumber() {
        return EcontactNumber;
    }

    public void setEcontactNumber(String econtactNumber) {
        EcontactNumber = econtactNumber;
    }

    public String getEtype() {
        return Etype;
    }

    public void setEtype(String etype) {
        Etype = etype;
    }
}
