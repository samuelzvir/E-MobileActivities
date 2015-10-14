package br.com.samuelzvir.meuabc.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import br.com.samuelzvir.meuabc.db.MeuABCDatabase;

@Table(databaseName = MeuABCDatabase.NAME)
public class AppConfiguration extends BaseModel  {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    String level;
    @Column
    String language;

    //TODO add user to configuration.

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}