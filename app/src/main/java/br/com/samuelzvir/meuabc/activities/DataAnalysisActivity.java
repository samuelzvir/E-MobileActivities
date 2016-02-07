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

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Student;

public class DataAnalysisActivity extends AppCompatActivity {
    private static final String TAG = "DataAnalysisActivity";
    final ArrayList<View> mCheckedViews = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        List<Student> studentsList = DataSupport.findAll(Student.class);
        final ListView users = (ListView) findViewById(R.id.studentslistView);
        final CheckBox usePositionsCB = new CheckBox(getApplicationContext());

        List<String> studentsNamesList = new ArrayList<>();
        for (Student s : studentsList){ // populates the words
            studentsNamesList.add(s.getNickname());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice,
                studentsNamesList);

        users.setAdapter(adapter);
        users.setItemsCanFocus(false);
        users.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                boolean checked = users.isItemChecked(position);
                if (checked) {
                    Log.i(TAG, "mCheckedViews.add(view);");
                    mCheckedViews.add(view);
                    //TODO list challenges.
                } else {
                    Log.i(TAG, "mCheckedViews.remove(view);");
                    mCheckedViews.remove(view);
                }
            }
        });
        mCheckedViews.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_abc, menu);
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

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context,
                                  int textViewResourceId,
                                  List<String> objects)
        {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        public Integer getId(String word){
            return mIdMap.get(word);
        }

        @Override
        public long getItemId(int position) {
            try{
                String item = getItem(position);
                return mIdMap.get(item);
            }catch(Exception e){
                return 0l;
            }

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    public void toStats(View view){
        Log.i(TAG,"stats about the user");
        Intent intent = new Intent(this,StatsActivity.class);
        startActivity(intent);
    }

}