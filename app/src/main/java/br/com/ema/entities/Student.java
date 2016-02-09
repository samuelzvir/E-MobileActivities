package br.com.ema.entities;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

public class Student  extends DataSupport implements Serializable {

    private int id;
    @Column(unique = true)
    private String nickname;
    @Column
    private String password;

    List<Challenge> challenges = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChallenges(List<Challenge> challenges) {

        this.challenges = challenges;
    }
}
