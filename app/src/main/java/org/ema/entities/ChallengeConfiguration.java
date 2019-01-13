package org.ema.entities;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ChallengeConfiguration extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Boolean retry;
    private String sorting;
    private RealmList<Challenge> challenges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRetry() {
        return retry;
    }

    public void setRetry(Boolean retry) {
        this.retry = retry;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public RealmList<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(RealmList<Challenge> challenges) {
        this.challenges = challenges;
    }
}
