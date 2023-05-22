package uk.ac.tees.aad.studentNumber;

import java.io.Serializable;

public class Hotel implements Serializable {
    private String hotelName;
    private String location;
    private String rating;
    private int price;
    private double longitude;
    private double latitude;
    private int id;
    private String desc;


    public Hotel(String hotelName, String location, String rating, int price, double longitude,
                 double latitude, int id, String desc) {
        this.hotelName = hotelName;
        this.location = location;
        this.rating = rating;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.desc = desc;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
