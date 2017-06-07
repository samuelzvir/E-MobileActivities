package br.com.ema.entities;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AppConfiguration extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Boolean showWord;
    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getShowWord() {
        return showWord;
    }

    public void setShowWord(Boolean showWord) {
        this.showWord = showWord;
    }
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}