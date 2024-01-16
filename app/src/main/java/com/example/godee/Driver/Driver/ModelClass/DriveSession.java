package com.example.godee.Driver.Driver.ModelClass;

import com.google.android.gms.maps.model.LatLng;

public class DriveSession {
    public enum DriverStatus{
        PENDING,
        ACCEPTED,
        CANCELLED,
        COMPLETED
    }
    private String sessionID;
    private String driverID;
    private String userID;
    private double startLocationLatitude;
    private double startLocationLongitude;
    private double endLocationLatitude;
    private double endLocationLongitude;
    private String addressInString;
    //    private Date date;
    public DriverStatus statusCode;

    public DriveSession(String driverID, String userID, double startLocationLatitude, double startLocationLongitude, double endLocationLatitude, double endLocationLongitude, String addressInString) {
        this.sessionID = driverID + "_" + userID;
        this.driverID = driverID;
        this.userID = userID;
        this.startLocationLatitude = startLocationLatitude;
        this.startLocationLongitude = startLocationLongitude;
        this.endLocationLatitude = endLocationLatitude;
        this.endLocationLongitude = endLocationLongitude;
        this.addressInString = addressInString;
        this.statusCode = DriverStatus.PENDING;
    }

    public DriveSession() {
    }


    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getStartLocationLatitude() {
        return startLocationLatitude;
    }

    public void setStartLocationLatitude(double startLocationLatitude) {
        this.startLocationLatitude = startLocationLatitude;
    }

    public double getStartLocationLongitude() {
        return startLocationLongitude;
    }

    public void setStartLocationLongitude(double startLocationLongitude) {
        this.startLocationLongitude = startLocationLongitude;
    }

    public double getEndLocationLatitude() {
        return endLocationLatitude;
    }

    public void setEndLocationLatitude(double endLocationLatitude) {
        this.endLocationLatitude = endLocationLatitude;
    }

    public double getEndLocationLongitude() {
        return endLocationLongitude;
    }

    public void setEndLocationLongitude(double endLocationLongitude) {
        this.endLocationLongitude = endLocationLongitude;
    }

    public String getAddressInString() {
        return addressInString;
    }

    public void setAddressInString(String addressInString) {
        this.addressInString = addressInString;
    }

    public DriverStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(DriverStatus statusCode) {
        this.statusCode = statusCode;
    }
}
