package br.com.samuelzvir.meuabc.entities.relations;

import org.litepal.crud.DataSupport;

public class StudentChallenge extends DataSupport{

    public StudentChallenge(int studentId, int challengeId) {
        this.studentId = studentId;
        this.challengeId = challengeId;
    }

    private int id;
    private int studentId;
    private  int challengeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
