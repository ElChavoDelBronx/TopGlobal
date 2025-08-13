package com.topglobal.dailyorder.models.objects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodOrder {
    private int orderId;
    private int diningTableId;
    private String diningTableName;  // Ej. "Mesa 5" o "Área Terraza"
    private int waiterId;
    private String waiterName;       // Ej. "Juan Pérez"
    private LocalDate orderDate;
    private double totalCost;
    private String orderStatus;
    private int dailyFolio;          // Folio diario
    private List<String> dishes;     // Lista tipo "2x Tacos"

    public FoodOrder() {
        dishes = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDiningTableId() {
        return diningTableId;
    }
    public void setDiningTableId(int diningTableId) {
        this.diningTableId = diningTableId;
    }

    public String getDiningTableName() {
        return diningTableName;
    }
    public void setDiningTableName(String diningTableName) {
        this.diningTableName = diningTableName;
    }

    public int getWaiterId() {
        return waiterId;
    }
    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    public String getWaiterName() {
        return waiterName;
    }
    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getDailyFolio() {
        return dailyFolio;
    }
    public void setDailyFolio(int dailyFolio) {
        this.dailyFolio = dailyFolio;
    }

    public List<String> getDishes() {
        return dishes;
    }
    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }
}

