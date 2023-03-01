package com.telelab.paperville.models;

import java.io.Serializable;

public class Student implements Serializable {


    String username;
    String email;
    String password;
    String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'';
    }
}
