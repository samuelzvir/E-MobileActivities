package br.com.ema.entities;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class PlayStats  extends DataSupport {

    private int id;
    private Date start;
    private Date end;
    private double totalPoints;
    private int userId;

    public PlayStats(Date start, int userId) {
        this.start = start;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getUserId() {
        return userId;
    }

    public void addPoint(int point){
        this.totalPoints+=point;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
