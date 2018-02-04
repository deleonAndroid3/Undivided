package com.training.android.undivided.GroupSender.Objects;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class ContactAdd {
    private String mPhone;
    private String mName;

    public ContactAdd() {
    }

    public ContactAdd(String phone, String name) {
        mPhone = phone;
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactAdd)) return false;

        ContactAdd contactAdd = (ContactAdd) o;

        if (!getPhone().equals(contactAdd.getPhone())) return false;
        return getName().equals(contactAdd.getName());

    }

    @Override
    public int hashCode() {
        int result = getPhone().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ContactAdd{" +
                "mPhone='" + mPhone + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }
}
