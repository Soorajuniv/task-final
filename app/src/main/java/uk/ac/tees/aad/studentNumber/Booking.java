package uk.ac.tees.aad.studentNumber;

public class Booking {
    private String hotelName;
    private int guests;
    private double price;
    private String checkInDate;
    private String checkOutDate;

    public Booking(String hotelName, int guests, double price, String checkInDate, String checkOutDate) {
        this.hotelName = hotelName;
        this.guests = guests;
        this.price = price;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getHotelName() {
        return hotelName;
    }

    public int getGuests() {
        return guests;
    }

    public double getPrice() {
        return price;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}
