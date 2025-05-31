package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {

    private Long employeeId;
    private Company company;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String employeeWorkId;
    private String passwordHash;
    private RoleEnum role;

    public Employee() {
    }

    public Employee(String employeeWorkId, String firstName, String lastName) {
        this.employeeWorkId = employeeWorkId;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    protected Employee(Parcel in) {
        if (in.readByte() == 0) {
            employeeId = null;
        } else {
            employeeId = in.readLong();
        }
        company = in.readParcelable(Company.class.getClassLoader());
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        employeeWorkId = in.readString();
        passwordHash = in.readString();
        role = RoleEnum.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (employeeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(employeeId);
        }
        dest.writeParcelable(company, flags);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(employeeWorkId);
        dest.writeString(passwordHash);
        dest.writeString(role.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    // Getters and Setters
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmployeeWorkId() {
        return employeeWorkId;
    }

    public void setEmployeeWorkId(String employeeWorkId) {
        this.employeeWorkId = employeeWorkId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
