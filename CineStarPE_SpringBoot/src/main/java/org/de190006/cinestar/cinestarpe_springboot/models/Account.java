package org.de190006.cinestar.cinestarpe_springboot.models;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    private String email;

    private String password;

    private int roleID;

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

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
