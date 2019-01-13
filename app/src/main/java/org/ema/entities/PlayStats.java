package org.ema.entities;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlayStats  extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Date start;
    private Date end;
    private String challengeId;
    private Boolean correct;
    private Boolean showWord;
    private Boolean voiceHelp;
    private Boolean playedTip;
    private Double totalPoints;
    private String userId;
    private String extra;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Boolean getShowWord() {
        return showWord;
    }

    public void setShowWord(Boolean showWord) {
        this.showWord = showWord;
    }

    public Boolean getVoiceHelp() {
        return voiceHelp;
    }

    public void setVoiceHelp(Boolean voiceHelp) {
        this.voiceHelp = voiceHelp;
    }

    public Boolean getPlayedTip() {
        return playedTip;
    }

    public void setPlayedTip(Boolean playedTip) {
        this.playedTip = playedTip;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
