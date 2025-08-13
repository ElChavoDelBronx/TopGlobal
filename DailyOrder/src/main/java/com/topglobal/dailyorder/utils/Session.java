package com.topglobal.dailyorder.utils;


import com.topglobal.dailyorder.models.users.Employee;

public class Session {
    private static Employee currentUser;

    public static void login(Employee employee) {
        currentUser = employee;
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}


