package com.topglobal.dailyorder.models.objects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodOrder {
    private int orderId;
    private int diningTableId;
    private int waiterId;
    private LocalDate orderDate;
    private double totalCost;
    private String orderStatus;

    // Nuevo: lista de platillos
    private List<MenuItem> items = new ArrayList<>();

    public FoodOrder() {}

    public FoodOrder(int orderId, LocalDate orderDate, String status) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = status;
    }

    // Getters & Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getDiningTableId() { return diningTableId; }
    public void setDiningTableId(int diningTableId) { this.diningTableId = diningTableId; }

    public int getWaiterId() { return waiterId; }
    public void setWaiterId(int waiterId) { this.waiterId = waiterId; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getOrderStatus() { return orderStatus; }
    public void setStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }
}
