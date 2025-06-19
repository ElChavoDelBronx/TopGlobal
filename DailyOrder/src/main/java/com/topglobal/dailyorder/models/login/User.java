package com.topglobal.dailyorder.models.login;

public abstract class User {
    private int id;
    private String name;
    private String fatherLastname;
    private String motherLastname;
    private String email;
    private String password;

    public User(int id, String name, String fatherLastname, String motherLastname, String email, String password) {
        this.id = id;
        this.name = name;
        this.fatherLastname = fatherLastname;
        this.motherLastname = motherLastname;
        this.email = email;
        this.password = password;
    }
}
