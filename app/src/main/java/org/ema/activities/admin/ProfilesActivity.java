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

package org.ema.activities.admin;

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

import java.util.ArrayList;
import java.util.List;

import org.ema.entities.Student;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProfilesActivity extends AppCompatActivity {
    private static final String TAG = "ProfilesActivity";
    List<String> studentNames = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(org.ema.R.layout.activity_profiles);
        Intent intent = getIntent();
        String userUpdated = intent.getStringExtra("updated");
        String userCreated = intent.getStringExtra("created");
        if(userCreated != null){
            TextView info = (TextView) findViewById(org.ema.R.id.profilesInfo);
            info.setTextColor(Color.GREEN);
            info.setText(userCreated + " " + getString(org.ema.R.string.wasCreated));
            info.setBackgroundColor(Color.TRANSPARENT);
        }else if(userUpdated != null){
            TextView info = (TextView) findViewById(org.ema.R.id.profilesInfo);
            info.setTextColor(Color.GREEN);
            info.setText(userUpdated +" "+ getString(org.ema.R.string.wasUpdated));
            info.setBackgroundColor(Color.TRANSPARENT);
        }
        listStudents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(org.ema.R.menu.menu_profiles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == org.ema.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listStudents(){
        RealmResults<Student> s = realm.where(Student.class).findAll();
        ListView  students = (ListView) findViewById(org.ema.R.id.studentslistView);
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
        ListView  students = (ListView) findViewById(org.ema.R.id.studentslistView);
        SparseBooleanArray checked = students.getCheckedItemPositions();
        int length = students.getCount();
        for (int i = 0;i < length ;i++){
            if(checked.get(i)){ //checked ?
                final String nickname = studentNames.get(i);
                Log.i(TAG, "Deleting user " + nickname);
                //delete all the tasks
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults< Student > result = realm.where(Student.class).equalTo("nickname", nickname).findAll();
                        result.deleteAllFromRealm();
                    }
                });
            }
        }
        listStudents();
    }

    public void toEditUser(View view){
        ListView  students = (ListView) findViewById(org.ema.R.id.studentslistView);
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
            TextView info = (TextView) findViewById(org.ema.R.id.profilesInfo);
            info.setTextColor(Color.YELLOW);
            info.setText(org.ema.R.string.checkOneToEdit);
            info.setBackgroundColor(Color.TRANSPARENT);
        }else{
            //message
            TextView info = (TextView) findViewById(org.ema.R.id.profilesInfo);
            info.setTextColor(Color.YELLOW);
            info.setText(org.ema.R.string.onlyOneToEdit);
            info.setBackgroundColor(Color.TRANSPARENT);
        }
        listStudents();
    }

    public void toMenu(View view){
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
