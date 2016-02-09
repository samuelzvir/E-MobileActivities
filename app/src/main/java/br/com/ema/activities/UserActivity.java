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

package br.com.ema.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import java.util.List;

import br.com.ema.R;
import br.com.ema.entities.Student;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        String studentName = intent.getStringExtra("studentName");
        if(studentName != null){
            List<Student> students = DataSupport.where("nickname = ?",studentName).find(Student.class);
            if(students.size() == 1){
                this.student = students.get(0);
                EditText nameET = (EditText) findViewById(R.id.username);
                nameET.setText(this.student.getNickname());
            }else{
                Log.w(TAG,"Found "+students.size()+" with name "+studentName);
                Log.w(TAG,"unable to edit");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void save(View view){
        if(student == null){
            EditText nameET = (EditText) findViewById(R.id.username);
            EditText passwordET = (EditText) findViewById(R.id.passwordField);
            String userName = nameET.getText().toString();
            String password = passwordET.getText().toString();
            Student student = new Student();
            student.setNickname(userName);
            student.setPassword(password);
            student.save();
            //redirects to users list
            Intent intent = new Intent(this, ProfilesActivity.class);
            intent.putExtra("created", student.getNickname());
            startActivity(intent);
        }else{
            EditText nameET = (EditText) findViewById(R.id.username);
            EditText passwordET = (EditText) findViewById(R.id.passwordField);
            String userName = nameET.getText().toString();
            String password = passwordET.getText().toString();
            this.student.setNickname(userName);
            this.student.setPassword(password);
            this.student.save();
            //redirects to users list
            Intent intent = new Intent(this, ProfilesActivity.class);
            intent.putExtra("updated",student.getNickname());
            startActivity(intent);
        }
    }

    public void toUsersList(View view){
        this.student = null;
        Intent intent = new Intent(this,ProfilesActivity.class);
        startActivity(intent);
    }

}
