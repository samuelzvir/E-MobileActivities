package br.com.ema.entities;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Admin extends DataSupport{

    private int id;
    @Column(unique = true, defaultValue = "unknown")
    private String name;
    @Column
    private String password;

    //Getters and Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

