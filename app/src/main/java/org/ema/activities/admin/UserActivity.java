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

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import org.ema.dialogs.CannotCreateUserAlreadyExists;
import org.ema.dialogs.CannotCreateUserWithAdminName;
import org.ema.entities.Admin;
import org.ema.entities.Student;

import io.realm.Realm;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    private Student student;
    private Realm realm;
    Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(org.ema.R.layout.activity_user);
        admin = realm.where(Admin.class).findFirst();
        Intent intent = getIntent();
        String studentName = intent.getStringExtra("studentName");
        if(studentName != null){
            List<Student> students = realm.where(Student.class).equalTo("nickname",studentName).findAll();
            if(students.size() == 1){
                this.student = students.get(0);
                EditText nameET = (EditText) findViewById(org.ema.R.id.username);
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
        getMenuInflater().inflate(org.ema.R.menu.menu_user, menu);
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

    public void save(View view){
       if(student == null){
            EditText nameET = (EditText) findViewById(org.ema.R.id.username);
            EditText passwordET = (EditText) findViewById(org.ema.R.id.passwordField);
            String userName = nameET.getText().toString();
            String password = passwordET.getText().toString();
            if(admin.getName().equalsIgnoreCase(userName)){
                CannotCreateUserWithAdminName cannotCreateUserWithAdminName = new CannotCreateUserWithAdminName();
                FragmentManager fm = getFragmentManager();
                cannotCreateUserWithAdminName.show(fm,"Choose another name");
            }else {

                List<Student> students = realm.where(Student.class).like("nickname", userName).findAll();
                if(students.size() > 0){
                    CannotCreateUserAlreadyExists cannotCreateUserAlreadyExists = new CannotCreateUserAlreadyExists();
                    FragmentManager fm = getFragmentManager();
                    cannotCreateUserAlreadyExists.show(fm,"Choose another name");
                }else{
                    final Student student = new Student();
                    student.setNickname(userName);
                    student.setPassword(password);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(student);
                        }
                    });
                    //redirects to users list
                    Intent intent = new Intent(this, ProfilesActivity.class);
                    intent.putExtra("created", student.getNickname());
                    startActivity(intent);
                }
            }
        }else{
            EditText nameET = (EditText) findViewById(org.ema.R.id.username);
            EditText passwordET = (EditText) findViewById(org.ema.R.id.passwordField);
            final String userName = nameET.getText().toString();
            final String password = passwordET.getText().toString();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    student.setNickname(userName);
                    student.setPassword(password);
                    realm.insertOrUpdate(student);
                }
            });
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

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
