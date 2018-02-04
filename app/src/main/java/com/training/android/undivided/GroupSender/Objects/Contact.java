package com.training.android.undivided.GroupSender.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class Contact {
    private String mName;
    private List<String> mPhones = new ArrayList<>();

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<String> getPhones() {
        return mPhones;
    }

    public void setPhones(List<String> phones) {
        mPhones = phones;
    }
}
