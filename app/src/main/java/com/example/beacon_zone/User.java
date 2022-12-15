package com.example.beacon_zone;

import java.util.Objects;

public class User {
    private String name;
    private String account;
    private String password;
    private String permission;
    private String email;

    public User(String name, String account, String password, String permission, String email) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.permission = permission;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
