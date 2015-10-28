package br.com.samuelzvir.meuabc.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import br.com.samuelzvir.meuabc.db.MeuABCDatabase;

@Table(databaseName = MeuABCDatabase.NAME)
public class Game  extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    private long id;

    private List<SimpleChallenge> simpleChallenges;
    @Column
    private double totalPoints;

    private Student student;

}
