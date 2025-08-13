package com.topglobal.dailyorder.utils;

import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuItem;
import com.topglobal.dailyorder.models.users.Employee;

import java.util.ArrayList;
import java.util.List;

public class SessionData {
    private Employee user;
    private List<DiningTable> diningTables = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<FoodOrder> foodOrders = new ArrayList<>();
    private DiningTable selectedTable;

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    public List<DiningTable> getDiningTables() {return diningTables;}
    public void setDiningTables(List<DiningTable> diningTables) {this.diningTables = diningTables;}
    public void addElement(DiningTable diningTable) {diningTables.add(diningTable);}
    public void setSelectedTable(DiningTable selectedTable) {this.selectedTable = selectedTable;}
    public DiningTable getSelectedTable() {return selectedTable;}

    public List<Employee> getEmployees() {return employees;}
    public void setEmployees(List<Employee> employees) {this.employees = employees;}
    public void addElement(Employee employee) {employees.add(employee);}

    public List<MenuItem> getMenuItems() {return menuItems;}
    public void setMenuItems(List<MenuItem> menuItems) {this.menuItems = menuItems;}
    public void addElement(MenuItem menuItem) {menuItems.add(menuItem);}

    public List<FoodOrder> getFoodOrders() {return foodOrders;}
    public void setFoodOrders(List<FoodOrder> foodOrders) {this.foodOrders = foodOrders;}
    public void addElement(FoodOrder foodOrder) {foodOrders.add(foodOrder);}

}
