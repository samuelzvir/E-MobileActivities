package org.ema.entities;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AppConfiguration extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String language;
    private Boolean sound;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getSound() {
        return sound;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }
}