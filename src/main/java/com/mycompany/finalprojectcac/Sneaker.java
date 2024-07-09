
package com.mycompany.finalprojectcac;

public class Sneaker {
    private int id_sneaker;
    private String brand;
    private String model;
    private String img;
    private int price;
    
    // Constructores
    public Sneaker(){
    }

    public Sneaker(int id_sneaker, String brand, String model, String img, int price) {
        this.id_sneaker = id_sneaker;
        this.brand = brand;
        this.model = model;
        this.img = img;
        this.price = price;
    }
    
    // Getters and Setters

    public int getId_sneaker() {
        return id_sneaker;
    }

    public void setId_sneaker(int id_sneaker) {
        this.id_sneaker = id_sneaker;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    
    
    
    
}
