package com.sofiane.envol.squarehangoutapp.Entities;

/**
 * Created by HP-450G3 on 18/07/2016.
 */
public class Stat {
    private String validate;
    private String sum_booking;
    private String sum_seats;

    private String name_user;
    private String id_booking;

    private String seats ;

    public Stat() {

    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getSum_booking() {
        return sum_booking;
    }

    public void setSum_booking(String sum_booking) {
        this.sum_booking = sum_booking;
    }

    public String getSum_seats() {
        return sum_seats;
    }

    public void setSum_seats(String sum_seats) {
        this.sum_seats = sum_seats;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getId_booking() {
        return id_booking;
    }

    public void setId_booking(String id_booking) {
        this.id_booking = id_booking;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}
