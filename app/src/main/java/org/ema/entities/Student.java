package org.ema.entities;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Student  extends RealmObject implements Serializable{

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String nickname;
    private String password;
    RealmList<ChallengeConfiguration> challenges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public RealmList<
            ChallengeConfiguration> getChallenges() {
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

    public void setChallenges(RealmList<ChallengeConfiguration> challenges) {
        this.challenges = challenges;
    }

}
