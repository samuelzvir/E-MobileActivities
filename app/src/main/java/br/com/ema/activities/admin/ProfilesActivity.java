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

package br.com.ema.activities.admin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import br.com.ema.R;
import br.com.ema.entities.Student;
import br.com.ema.entities.relations.StudentChallenge;

public class ProfilesActivity extends AppCompatActivity {
    private static final String TAG = "ProfilesActivity";
    List<String> studentNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        Intent intent = getIntent();
        String userUpdated = intent.getStringExtra("updated");
        String userCreated = intent.getStringExtra("created");
        if(userCreated != null){
            TextView info = (TextView) findViewById(R.id.profilesInfo);
            info.setTextColor(Color.GREEN);
            info.setText(userCreated + " " + getString(R.string.wasCreated));
            info.setBackgroundColor(Color.TRANSPARENT);
        }else if(userUpdated != null){
            TextView info = (TextView) findViewById(R.id.profilesInfo);
            info.setTextColor(Color.GREEN);
            info.setText(userUpdated +" "+ getString(R.string.wasUpdated));
            info.setBackgroundColor(Color.TRANSPARENT);
        }
        listStudents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profiles, menu);
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

    private void listStudents(){
        List<Student> s = DataSupport.findAll(Student.class);
        ListView  students = (ListView) findViewById(R.id.studentslistView);
        studentNames.clear();
        for (Student tempStudent : s){ // populates the usernames
            studentNames.add(tempStudent.getNickname());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                studentNames );
        students.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void toUserSettings(View view){
        Intent intent = new Intent(this,UserActivity.class);
        startActivity(intent);
    }

    /**
     * Method used to remove users from database.
     */
    public void deleteUsers(View view){
        ListView  students = (ListView) findViewById(R.id.studentslistView);
        SparseBooleanArray checked = students.getCheckedItemPositions();
        int length = students.getCount();
        for (int i = 0;i < length ;i++){
            if(checked.get(i)){ //checked ?
                String nickname = studentNames.get(i);
                Log.i(TAG, "Deleting user " + nickname);
                Student student = DataSupport.where("nickname = ?",nickname).find(Student.class).get(0);
                //delete all the tasks
                int deletedRelations = DataSupport.deleteAll(StudentChallenge.class,"studentId = ?",student.getId()+"");
                Log.d(TAG, "Deleted "+deletedRelations+" word tasks for user" + nickname);
                //delete user from the base
                int rowsDeleted = DataSupport.delete(Student.class,student.getId());
                if(rowsDeleted > 0){
                    Log.i(TAG, "User " + nickname+" deleted.");
                }
            }
        }
        listStudents();
    }

    public void toEditUser(View view){
        ListView  students = (ListView) findViewById(R.id.studentslistView);
        SparseBooleanArray checked = students.getCheckedItemPositions();
        int checkedItems =  checked.size();
        if(checkedItems == 1){
            int length = students.getCount();
            int i = 0;
            do{
                if(checked.get(i)) { //checked ?
                    Intent intent = new Intent(this,UserActivity.class);
                    intent.putExtra("studentName", studentNames.get(i));
                    startActivity(intent);
                }
                i++;
            }while(i < length);
        }else if(checkedItems == 0){
            TextView info = (TextView) findViewById(R.id.profilesInfo);
            info.setTextColor(Color.YELLOW);
            info.setText(R.string.checkOneToEdit);
            info.setBackgroundColor(Color.TRANSPARENT);
        }else{
            //message
            TextView info = (TextView) findViewById(R.id.profilesInfo);
            info.setTextColor(Color.YELLOW);
            info.setText(R.string.onlyOneToEdit);
            info.setBackgroundColor(Color.TRANSPARENT);
        }
        listStudents();
    }

    public void toMenu(View view){
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
    }

}
