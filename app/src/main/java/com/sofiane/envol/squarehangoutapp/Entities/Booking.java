package com.sofiane.envol.squarehangoutapp.Entities;

/**
 * Created by HP-450G3 on 20/06/2016.
 */
public class Booking {

   private String date_book;
    private String resto_book;
    private String seats;
    private String time_book;
    private String name_user ;

    private String deleted ;
    private String info ;

    private String idUser;
    private String idPlace;
    private String idBooking;

    private String open;
    private String close;

    @SuppressWarnings("unused")
    public Booking() {

    }


    public String getDate_book() {
        return date_book;
    }

    public void setDate_book(String date_book) {
        this.date_book = date_book;
    }


    public String getResto_book() {
        return resto_book;
    }

    public void setResto_book(String resto_book) {
        this.resto_book = resto_book;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTime_book() {
        return time_book;
    }

    public void setTime_book(String time_book) {
        this.time_book = time_book;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}