package com.topglobal.dailyorder.models.login;

public class Admin extends Employee{
    public Admin(int id, String name, String fatherLastname, String motherLastname, String email, String password, String phoneNumber, String role, String shift, String status, float salary) {
        super(id, name, fatherLastname, motherLastname, email, password, phoneNumber, role, shift, status, salary);
    }

    public Admin(int id, String name, String fatherLastname, String motherLastname, String role, String status) {
        super(id, name, fatherLastname, motherLastname, role, status);
    }
}
