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
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import br.com.samuelzvir.meuabc.db.MeuABCDatabase;

@Table(databaseName = MeuABCDatabase.NAME)
public class Challenge extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String name;
    @Column
    private String text;


    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "student_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false)
    ForeignKeyContainer<Student> studentModelContainer;

    /**
     * setting the model for the student.
     */
    public void associateQueen(Student student) {
        studentModelContainer = new ForeignKeyContainer<>(Student.class);
        studentModelContainer.setModel(student);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}