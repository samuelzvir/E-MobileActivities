package br.com.ema.entities;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class SimpleChallenge  extends DataSupport {

    private int id;
    @Column
    String word;
    @Column
    String imagePath;
    @Column
    String wordType;
    @Column
    double points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
