package com.topglobal.dailyorder.utils;

import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuCategory;
import com.topglobal.dailyorder.models.objects.MenuItem;
import com.topglobal.dailyorder.models.users.Employee;

import java.util.ArrayList;
import java.util.List;

public class SessionData {
    private Employee user;
    private List<DiningTable> diningTables = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<MenuCategory> menuCategories = new ArrayList<>();
    private List<FoodOrder> foodOrders = new ArrayList<>();
    private DiningTable selectedTable;
    private MenuItem selectedMenuItem;

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    public List<DiningTable> getDiningTables() {return diningTables;}
    public void setDiningTables(List<DiningTable> diningTables) {this.diningTables = diningTables;}
    public void addElement(DiningTable diningTable) {diningTables.add(diningTable);}
    public void removeElement(DiningTable diningTable) {diningTables.remove(diningTable);}
    public void setSelectedTable(DiningTable selectedTable) {this.selectedTable = selectedTable;}
    public DiningTable getSelectedTable() {return selectedTable;}

    public List<Employee> getEmployees() {return employees;}
    public void setEmployees(List<Employee> employees) {this.employees = employees;}
    public void addElement(Employee employee) {employees.add(employee);}
    public void removeElement(Employee employee) {employees.remove(employee);}

    public List<MenuItem> getMenuItems() {return menuItems;}
    public void setMenuItems(List<MenuItem> menuItems) {this.menuItems = menuItems;}
    public void addElement(MenuItem menuItem) {menuItems.add(menuItem);}
    public void removeElement(MenuItem menuItem) {menuItems.remove(menuItem);}
    public MenuItem getSelectedMenuItem() {return selectedMenuItem;}
    public void setSelectedMenuItem(MenuItem selectedMenuItem) {this.selectedMenuItem = selectedMenuItem;}

    public List<MenuCategory> getMenuCategories() {return menuCategories;}
    public void setMenuCategories(List<MenuCategory> menuCategories) {this.menuCategories = menuCategories;}

    public List<FoodOrder> getFoodOrders() {return foodOrders;}
    public void setFoodOrders(List<FoodOrder> foodOrders) {this.foodOrders = foodOrders;}
    public void addElement(FoodOrder foodOrder) {foodOrders.add(foodOrder);}
    public void removeElement(FoodOrder foodOrder) {foodOrders.remove(foodOrder);}

}
