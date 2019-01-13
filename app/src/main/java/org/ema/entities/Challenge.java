package org.ema.entities;

import java.io.Serializable;
import java.util.UUID;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Challenge associated to user(s)
 */
public class Challenge  extends RealmObject implements Serializable{

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Boolean showWord;
    private String text;
    private Integer imageRotation;
    private Double points;
    private ChallengeResource resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShowWord() {
        return showWord;
    }

    public void setShowWord(Boolean showWord) {
        this.showWord = showWord;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getImageRotation() {
        return imageRotation;
    }

    public void setImageRotation(Integer imageRotation) {
        this.imageRotation = imageRotation;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public ChallengeResource getResource() {
        return resource;
    }

    public void setResource(ChallengeResource resource) {
        this.resource = resource;
    }
}