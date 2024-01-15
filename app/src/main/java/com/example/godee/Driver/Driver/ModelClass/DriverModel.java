package com.example.godee.Driver.Driver.ModelClass;


import java.util.ArrayList;

public class DriverModel {
    private String name;
    private String email;
    private String phoneNumber;
    private String currentUserID;
    private int rating;
    private int age;
    private String nationality;
    private int accountType;
    private Double latitude, longitude;
    private Boolean inSession;
    private ArrayList<DriveSession> driverAllSession = new ArrayList<>();

    public DriverModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public DriverModel(String name, String email, String phoneNumber, int age, String nationality, int accountType) {
        this.name = name;
        this.email = email;
        this.currentUserID = "";
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.rating = 5;
        this.nationality = nationality;
        this.accountType = accountType;
        this.inSession = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public Boolean getInSession() {
        return inSession;
    }

    public void setInSession(Boolean inSession) {
        this.inSession = inSession;
    }

    public ArrayList<DriveSession> getDriverAllSession() {
        return driverAllSession;
    }

    public void setDriverAllSession(ArrayList<DriveSession> driverAllSession) {
        this.driverAllSession = driverAllSession;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
