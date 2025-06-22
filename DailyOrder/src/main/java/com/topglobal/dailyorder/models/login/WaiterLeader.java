package com.topglobal.dailyorder.models.login;

public class WaiterLeader extends Employee{
    public WaiterLeader(int id, String name, String fatherLastname, String motherLastname, String email, String password, String phoneNumber, String role, String shift, String status, float salary) {
        super(id, name, fatherLastname, motherLastname, email, password, phoneNumber, role, shift, status, salary);
    }

    public WaiterLeader(int id, String name, String fatherLastname, String motherLastname, String role, String status) {
        super(id, name, fatherLastname, motherLastname, role, status);
    }
}
