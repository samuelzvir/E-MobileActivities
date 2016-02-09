package br.com.ema.entities;


import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class AppConfiguration extends DataSupport {

    private int id;
    @Column
    String level;
    @Column
    String language;

    //TODO add user to configuration.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}