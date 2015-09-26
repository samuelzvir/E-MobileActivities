/*
 * Copyright 2015 Samuel Yuri Zvir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.samuelzvir.meuabc.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.HashMap;
import java.util.List;

import br.com.samuelzvir.meuabc.db.MeuABCDatabase;

@ModelContainer
@Table(databaseName = MeuABCDatabase.NAME)
public class Student extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String nickname;
    @Column
    private String password;

    List notes; // list of strings
    List<Challenge> challenges;

    public String getNickname() {
        return nickname;
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

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void setChallenges(List<Challenge> challenges) {

        this.challenges = challenges;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToMany(methods = {OneToMany.Method.ALL, OneToMany.Method.DELETE}, variableName = "challenges")
    public List<Challenge> getMyChallenges() {
        if(challenges == null) {
            challenges = new Select()
                    .from(Challenge.class)
                    .where(Condition.column(Challenge$Table.STUDENTMODELCONTAINER_STUDENT_ID).is(id))
                    .queryList();
        }
        return challenges;
    }
    @Override
    public void save(){
        super.save();
        if(challenges != null) {
            for (Challenge challenge : challenges) {
                challenge.associateQueen(this);
                challenge.save();
            }
        }
    }
}
