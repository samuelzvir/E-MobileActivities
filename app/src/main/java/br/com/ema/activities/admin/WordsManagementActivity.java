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

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;


import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;

import br.com.ema.activities.admin.CreateTextActivity;
import br.com.ema.entities.SimpleChallenge;
import br.com.ema.R;

public class WordsManagementActivity extends AppCompatActivity {

    private static final String TAG = "WordsManagementActivity";
    final ArrayList<View> mCheckedViews = new ArrayList<>();
    final List<String> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_words);
        List<SimpleChallenge> simpleChallenges = DataSupport.findAll(SimpleChallenge.class);
        final ListView challenges = (ListView) findViewById(R.id.wordsListView);
        final Button deleteButton = (Button) findViewById(R.id.deleteButton);
        final CheckBox usePositionsCB = new CheckBox(getApplicationContext());

        List<String> challengesList = new ArrayList<>();
        for (SimpleChallenge challenge : simpleChallenges){ // populates the words
            challengesList.add(challenge.getWord());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice,
                challengesList);

        challenges.setAdapter(adapter);
        challenges.setItemsCanFocus(false);
        challenges.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListView challengesChange = (ListView) findViewById(R.id.wordsListView);
                SparseBooleanArray checkedItems = challengesChange.getCheckedItemPositions();
                int numCheckedItems = checkedItems.size();
                Log.i(TAG, numCheckedItems + "checked items.");
                for (int i = numCheckedItems - 1; i >= 0; --i) {
                    if (!checkedItems.valueAt(i)) {
                        continue;
                    }
                    int position = checkedItems.keyAt(i);
                    final String item = adapter.getItem(position);
                    if (!usePositionsCB.isChecked()) {
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //TODO
                                Log.i(TAG, "removing item " + item);
                                adapter.remove(item);
                                deleteWord(item);
                            }
                        }, 300);
                    } else {
                        mCheckedViews.clear();
                        int positionOnScreen = position - challengesChange.getFirstVisiblePosition();
                        if (positionOnScreen >= 0 &&
                                positionOnScreen < challengesChange.getChildCount()) {
                            final View view = challengesChange.getChildAt(positionOnScreen);
                            view.animate().alpha(0).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    view.setAlpha(1);
                                    Log.i(TAG, "removing item " + item);
                                    adapter.remove(item);
                                    deleteWord(item);
                                }
                            });
                        } else {
                            v.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG, "removing item " + item);
                                    adapter.remove(item);
                                    deleteWord(item);
                                }
                            }, 300);
                        }
                    }
                }
                Log.i(TAG, "No item checked.");
            }
        });

        challenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                boolean checked = challenges.isItemChecked(position);
                if (checked) {
                    Log.i(TAG, "mCheckedViews.add(view);");
                    mCheckedViews.add(view);
                } else {
                    Log.i(TAG, "mCheckedViews.remove(view);");
                    mCheckedViews.remove(view);
                }
            }
        });
        mCheckedViews.clear();
        adapter.notifyDataSetChanged();
        listWords();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_build_challenges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteWord(String word){
        Log.i(TAG, "word " + word);
        List<SimpleChallenge>  challenges =  DataSupport.where("word = ?", word).find(SimpleChallenge.class);

        for (SimpleChallenge simpleChallenge : challenges){
            if(simpleChallenge.getImagePath() != null){
                deleteFromDisk(simpleChallenge.getImagePath());
            }
            simpleChallenge.delete();
        }
        //TODO
    }

    public void addWord(View view){
        Intent intent = new Intent(this,CreateTextActivity.class);
        startActivity(intent);
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

    private void deleteFromDisk(String path){
        boolean deleted = new File(path).delete();
        if(deleted){
            Log.i(TAG,"file "+path+" deleted.");
        }
    }

    public void updateWord(View view){
        ListView  wordsListView = (ListView) findViewById(R.id.wordsListView);
        SparseBooleanArray checked = wordsListView.getCheckedItemPositions();
        int checkedItems =  checked.size();
        if(checkedItems == 1){
            int length = wordsListView.getCount();
            int i = 0;
            do{
                if(checked.get(i)) { //checked ?
                    Intent intent = new Intent(this,CreateTextActivity.class);
                    intent.putExtra("text", words.get(i));
                    startActivity(intent);
                }
                i++;
            }while(i < length);
        }else if(checkedItems == 0){
            //TODO dialog in the future
//            TextView info = (TextView) findViewById(R.id.);
//            info.setTextColor(Color.YELLOW);
//            info.setText(R.string.);
//            info.setBackgroundColor(Color.TRANSPARENT);
        }else{
            //message
            //TODO dialog in the future
//            TextView info = (TextView) findViewById(R.id.);
//            info.setTextColor(Color.YELLOW);
//            info.setText(R.string.);
//            info.setBackgroundColor(Color.TRANSPARENT);
        }
        listWords();
    }


    private void listWords(){
        List<SimpleChallenge> s = DataSupport.findAll(SimpleChallenge.class);
        ListView  wordsListView = (ListView) findViewById(R.id.wordsListView);
        words.clear();
        for (SimpleChallenge simpleChallenge : s){ // populates the words
            words.add(simpleChallenge.getWord());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                words );
        wordsListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
}
