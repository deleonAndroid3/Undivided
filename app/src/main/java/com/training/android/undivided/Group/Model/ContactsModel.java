package com.training.android.undivided.Group.Model;

/**
 * Created by Dyste on 9/30/2017.
 */

public class ContactsModel {

    private String ContactName;
    private String ContactNumber;
    private int fk_id;

    public ContactsModel() {
    }

    public ContactsModel(String contactName, String contactNumber, int fk_id) {
        ContactName = contactName;
        ContactNumber = contactNumber;
        this.fk_id = fk_id;
    }

    public String getContactName() {
        return ContactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public int getFk_id() {
        return fk_id;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public void setFk_id(int fk_id) {
        this.fk_id = fk_id;
    }
}
