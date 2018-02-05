package com.training.android.undivided.GroupSender.Objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class SMS implements Parcelable {
    private String organization;
    private String signature;
    private String message;

    public static class Builder {
        private String organization = null;
        private String signature = null;
        private String message = null;

        public Builder setOrganization(String organization) {
            if (!TextUtils.isEmpty(organization))
                this.organization = "[" + organization + "]";
            else
                this.organization = organization;
            return this;
        }

        public Builder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public SMS create() {
            return new SMS(organization, signature, message);
        }
    }

    private SMS(String organization, String signature, String message) {
        this.organization = organization;
        this.signature = signature;
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(organization);
        dest.writeString(signature);
        dest.writeString(message);

    }
    public static final Parcelable.Creator<SMS> CREATOR
            = new Parcelable.Creator<SMS>() {

        @Override
        public SMS createFromParcel(Parcel source) {
            return new SMS.Builder()
                    .setOrganization(source.readString())
                    .setSignature(source.readString())
                    .setMessage(source.readString())
                    .create();
        }

        @Override
        public SMS[] newArray(int size) {
            return new SMS[size];
        }
    };

    @Override
    public String toString() {
        boolean isOrgNotEmpty = true;
        boolean isSigNotEmpty = true;
        if (TextUtils.isEmpty(organization)) {
            isOrgNotEmpty = false;
        }
        if (TextUtils.isEmpty(signature)) {
            isSigNotEmpty = false;
        }
        return organization
                + (isOrgNotEmpty && isSigNotEmpty ? "-" : "")
                + signature
                + (isOrgNotEmpty || isSigNotEmpty ? ":" : "")
                + message;
    }
}
