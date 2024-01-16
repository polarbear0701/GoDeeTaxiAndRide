package com.example.godee.Driver.Driver.ModelClass;


import java.util.ArrayList;
import java.util.List;

public class DriverModel {
    private String name;
    private String email;
    private String phoneNumber;
    private int age;
    private String currentGuest;
    private String nationality;
    private int rating;
    private int accountType;
    private Double latitude, longitude;
    private Boolean inSession;
    private List<DriveSession> driverAllSession = new ArrayList<>();

    public DriverModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public DriverModel(String name, String email, String phoneNumber, int age, String nationality, int accountType) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.nationality = nationality;
        this.accountType = accountType;
        this.inSession = false;
        this.rating = 5;
        this.currentGuest = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public String getCurrentGuest() {
        return currentGuest;
    }

    public void setCurrentGuest(String currentGuest) {
        this.currentGuest = currentGuest;
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

    public List<DriveSession> getDriverAllSession() {
        return driverAllSession;
    }

//    public void setDriverAllSession(ArrayList<DriveSession> driverAllSession) {
//        this.driverAllSession = driverAllSession;
//    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDriverAllSession(List<DriveSession> driverAllSession) {
        this.driverAllSession = driverAllSession;
    }
}
