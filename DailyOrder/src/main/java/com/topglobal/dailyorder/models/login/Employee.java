package com.topglobal.dailyorder.models.login;

public class Employee {
    private int id;
    private String name;
    private String fatherLastname;
    private String motherLastname;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private String shift;
    private String status;
    private float salary;

    public Employee(int id, String name, String fatherLastname, String motherLastname,
                    String email, String password, String phoneNumber, String role,
                    String shift, String status, float salary) {
        this.id = id;
        this.name = name;
        this.fatherLastname = fatherLastname;
        this.motherLastname = motherLastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.shift = shift;
        this.status = status;
        this.salary = salary;
    }
    public Employee(int id, String name, String fatherLastname, String motherLastname,
                    String role, String status){
        this.id = id;
        this.name = name;
        this.fatherLastname = fatherLastname;
        this.motherLastname = motherLastname;
        this.role = role;
        this.status = status;
    }
}
