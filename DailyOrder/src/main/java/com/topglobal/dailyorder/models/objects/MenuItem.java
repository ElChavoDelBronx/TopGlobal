package com.topglobal.dailyorder.models.objects;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private double cost;
    private String image;
    private MenuCategory category;
    private int isActive;
    private int quantity;

    // 🔹 Constructor vacío: útil cuando quieres instanciar y luego settear valores
    public MenuItem() {
    }

    // 🔹 Constructor con parámetros: inicializa directamente
    public MenuItem(int idItem, String name, double price) {
        this.id = idItem;
        this.name = name;
        this.cost = price;
    }

    // ====== Getters & Setters ======

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public void setCategory(MenuCategory category) {
        this.category = category;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}