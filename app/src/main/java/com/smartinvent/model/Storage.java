package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Storage implements Parcelable {
    private Long storageId;
    private Company company;
    private String name;
    private String location;
    private String details;

    public Storage() {
    }

    public Storage(Long storageId, Company company, String name, String location, String details) {
        this.storageId = storageId;
        this.company = company;
        this.name = name;
        this.location = location;
        this.details = details;
    }

    protected Storage(Parcel in) {
        if (in.readByte() == 0) {
            storageId = null;
        } else {
            storageId = in.readLong();
        }
        company = in.readParcelable(Company.class.getClassLoader());
        name = in.readString();
        location = in.readString();
        details = in.readString();
    }

    public static final Creator<Storage> CREATOR = new Creator<Storage>() {
        @Override
        public Storage createFromParcel(Parcel in) {
            return new Storage(in);
        }

        @Override
        public Storage[] newArray(int size) {
            return new Storage[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (storageId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(storageId);
        }
        dest.writeParcelable(company, flags);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(details);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }
}
