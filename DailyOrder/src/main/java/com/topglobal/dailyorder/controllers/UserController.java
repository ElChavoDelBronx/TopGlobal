package com.topglobal.dailyorder.controllers;

import com.topglobal.dailyorder.models.users.Employee;

public abstract class UserController {
    protected Employee user;

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }
    public abstract void setInfo();

}
