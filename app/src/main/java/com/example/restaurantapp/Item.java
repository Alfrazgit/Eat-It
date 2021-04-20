package com.example.restaurantapp;

public class Item {

    String Name, Pic, Cost;

    public Item() {
    }

    public Item(String name, String pic, String cost) {
        Name = name;
        Pic = pic;
        Cost = cost;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }
}
