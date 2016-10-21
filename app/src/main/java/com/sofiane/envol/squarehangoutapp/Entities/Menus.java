package com.sofiane.envol.squarehangoutapp.Entities;

/**
 * Created by HP-450G3 on 08/06/2016.
 */
public class Menus {

    public String menu_description;
    public String menu_id;
    public String menu_name;
    public String menu_price;
    public String menu_type;
    public String id_restaurant;


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
   public Menus() {
    }


    public String getMenu_description() {
        return menu_description;
    }

    public void setMenu_description(String menu_description) {
        this.menu_description = menu_description;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(String id_restaurant) {
        this.id_restaurant = id_restaurant;
    }
}
