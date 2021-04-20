package com.example.restaurantapp;

public class CartItem {

    String pCategory, pName, pId, pCost, pQuantity;

    public CartItem() {

    }

    public CartItem(String pCategory, String pName, String pId, String pCost, String pQuantity) {
        this.pCategory = pCategory;
        this.pName = pName;
        this.pId = pId;
        this.pCost = pCost;
        this.pQuantity = pQuantity;
    }

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpCost() {
        return pCost;
    }

    public void setpCost(String pCost) {
        this.pCost = pCost;
    }

    public String getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }
}
