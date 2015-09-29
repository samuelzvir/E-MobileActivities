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

package br.com.samuelzvir.meuabc.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Challenge;
import br.com.samuelzvir.meuabc.entities.Student;

public class MenuActivity extends Activity {

    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FlowManager.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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

    public void onToMessages(View view){
        Intent intent = new Intent(this, CreateMessageActivity.class);
        startActivity(intent);
    }

    public void onToCamera(View view){
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }

    public void onCreateUser(View view){
        Student student = new Student();
        student.setNickname("Cisco");
        Challenge challenge = new Challenge();
        challenge.setName("Challenge of Vegetables");
        List notes = new ArrayList<String>();
        notes.add("this is a test note");

        student.setNotes(notes);
        student.save();

        List<Student> s = new Select().from(Student.class).queryList();

        for (Student studt : s){
            Log.i(TAG, "Student: ");
            Log.i(TAG, " -ID: "+ studt.getId());
            Log.i(TAG, " -name: "+ studt.getNickname());
            if(studt.getMyChallenges().size() > 0){
                Log.i(TAG, " -Challenge: "+ studt.getMyChallenges().get(0).getName());
            }

            if(studt.getNotes() != null && studt.getNotes().size() > 0){
                Log.i(TAG, " -My note: " + studt.getNotes().get(0));
            }
        }
    }
}
