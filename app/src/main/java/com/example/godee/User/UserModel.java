package com.example.godee.User;

import com.example.godee.Driver.Driver.ModelClass.DriveSession;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private String name;
    private String email;
    private String phoneNumber;
    private int age;
    private String nationality;
    private userAddress useraddress;
    private int accountType;
    private String currentDriverID;
    private Double latitude, longitude;
    private int rating;
    private Boolean inRide;
    private List<DriveSession> userAllSession = new ArrayList<>();

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserModel(String name, String email, String phoneNumber, int age, String nationality, userAddress useraddress, int accountType) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.nationality = nationality;
        this.useraddress = useraddress;
        this.accountType = accountType;
        this.currentDriverID = "";
        this.inRide = false;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.rating = 5;
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

    public userAddress getAddress() {
        return useraddress;
    }

    public void setAddress(userAddress useraddress) {
        this.useraddress = useraddress;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Boolean getInRide() {
        return inRide;
    }

    public void setInRide(Boolean inRide) {
        this.inRide = inRide;
    }

    public userAddress getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(userAddress useraddress) {
        this.useraddress = useraddress;
    }

    public List<DriveSession> getUserAllSession() {
        return userAllSession;
    }

    public void setUserAllSession(ArrayList<DriveSession> userAllSession) {
        this.userAllSession = userAllSession;
    }

    public String getCurrentDriverID() {
        return currentDriverID;
    }

    public void setCurrentDriverID(String currentDriverID) {
        this.currentDriverID = currentDriverID;
    }
}

//package com.example.godee;

class userAddress {
    private String addressNumber;
    private String street;
    private String ward; // Optional
    private String district;
    private String city;
    private String country; // Optional

    public userAddress(String addressNumber, String street, String ward, String district, String city, String country) {
        this.addressNumber = addressNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.country = country;
    }

    public userAddress(){}

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String convertStringAddress(){
        return addressNumber + " " + street + " " + ward + " " + district + " " + city + " " + country;
    }
}

