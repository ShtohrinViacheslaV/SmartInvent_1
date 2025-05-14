package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Company implements Parcelable {
    private Long companyId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String createdAt;

    public Company() {
    }

    public Company(String name, String address, String phone, String email, String createdAt) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Company(Long companyId, String name, String address, String phone, String email, String createdAt) {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    protected Company(Parcel in) {
        if (in.readByte() == 0) {
            companyId = null;
        } else {
            companyId = in.readLong();
        }
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        email = in.readString();
        createdAt = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (companyId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(companyId);
        }
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(createdAt);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

}
