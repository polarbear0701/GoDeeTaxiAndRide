package com.example.godee.ModelClass;


public class DriverModel {
    private String name;
    private String email;
    private String phoneNumber;
    private int age;
    private String nationality;
    private Double latitude, longitude;

    public DriverModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public DriverModel(String name, String email, String phoneNumber, int age, String nationality) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.nationality = nationality;
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
}
