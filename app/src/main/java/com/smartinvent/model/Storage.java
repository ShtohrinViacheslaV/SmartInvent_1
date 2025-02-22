package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Storage implements Parcelable {
    private Long storageId;
    private String name;
    private String location;
    private String details;

    public Storage() {
    }

    public Storage(Long storageId) {
        this.storageId = storageId;
    }

    protected Storage(Parcel in) {
        if (in.readByte() == 0) {
            storageId = null;
        } else {
            storageId = in.readLong();
        }
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
        return name; // Для відображення в Spinner
    }
}