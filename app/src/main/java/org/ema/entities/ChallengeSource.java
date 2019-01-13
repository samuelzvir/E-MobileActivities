package org.ema.entities;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ChallengeSource  extends RealmObject implements Serializable{

    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    private String word;
    private Integer imageRotation;
    private String wordType;
    private ChallengeResource resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getImageRotation() {
        return imageRotation;
    }

    public void setImageRotation(Integer imageRotation) {
        this.imageRotation = imageRotation;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public ChallengeResource getResource() {
        return resource;
    }

    public void setResource(ChallengeResource resource) {
        this.resource = resource;
    }
}
