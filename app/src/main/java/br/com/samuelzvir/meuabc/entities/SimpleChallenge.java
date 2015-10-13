package br.com.samuelzvir.meuabc.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import br.com.samuelzvir.meuabc.db.MeuABCDatabase;

@Table(databaseName = MeuABCDatabase.NAME)
public class SimpleChallenge extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    String word;
    @Column
    String imagePath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
