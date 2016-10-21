package com.sofiane.envol.squarehangoutapp.Entities;

public class NightClubs {

    private String address;
    private String close_time;
    private String lng;
    private String lat;

    private String name;
    private String nbr_booking;
    private String open_time;
    private String phone;

    private String image;

    private String id;

    private String owner;


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public NightClubs() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNbr_booking() {
        return nbr_booking;
    }

    public void setNbr_booking(String nbr_booking) {
        this.nbr_booking = nbr_booking;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}