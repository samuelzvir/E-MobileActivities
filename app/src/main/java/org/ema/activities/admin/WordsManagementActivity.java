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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ema.entities.ChallengeSource;

import io.realm.Realm;
import io.realm.RealmResults;

public class WordsManagementActivity extends AppCompatActivity {

    private static final String TAG = "WordsManagementActivity";
    final ArrayList<View> mCheckedViews = new ArrayList<>();
    final List<String> words = new ArrayList<>();
    private List<String> challengesList = new ArrayList<>();
    private ListView challenges;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(org.ema.R.layout.activity_manage_words);
        final Button deleteButton = (Button) findViewById(org.ema.R.id.deleteButton);
        final CheckBox usePositionsCB = new CheckBox(getApplicationContext());
        final StableArrayAdapter adapter = getAdapter();
        this.challenges = (ListView) findViewById(org.ema.R.id.wordsListView);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListView challengesChange = (ListView) findViewById(org.ema.R.id.wordsListView);
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
                                Log.i(TAG, "removing item " + item);
                                adapter.remove(item);
                                deleteWord(item);
                                adapter.notifyDataSetChanged();
                                ListView  wordsListView = (ListView) findViewById(org.ema.R.id.wordsListView);
                                wordsListView.setAdapter(adapter);
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
                                    adapter.notifyDataSetChanged();
                                    ListView  wordsListView = (ListView) findViewById(org.ema.R.id.wordsListView);
                                    wordsListView.setAdapter(adapter);
                                }
                            });
                        } else {
                            v.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG, "removing item " + item);
                                    adapter.remove(item);
                                    deleteWord(item);
                                    adapter.notifyDataSetChanged();
                                    ListView  wordsListView = (ListView) findViewById(org.ema.R.id.wordsListView);
                                    wordsListView.setAdapter(adapter);
                                }
                            }, 300);
                        }
                    }
                }
                Log.i(TAG, "No item checked.");
            }
        });

        this.challenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        StableArrayAdapter adapter = getAdapter();
        this.challenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
        getMenuInflater().inflate(org.ema.R.menu.menu_build_challenges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == org.ema.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteWord(final String word){
        Log.i(TAG, "word " + word);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ChallengeSource> result = realm.where(ChallengeSource.class).equalTo("word", word).findAll();
                result.deleteAllFromRealm();
            }
        });
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

    public void updateWord(View view){
        ListView  wordsListView = (ListView) findViewById(org.ema.R.id.wordsListView);
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
        List<ChallengeSource> s = realm.where(ChallengeSource.class).findAll();
        ListView  wordsListView = (ListView) findViewById(org.ema.R.id.wordsListView);
        words.clear();
        for (ChallengeSource simpleChallenge : s){ // populates the words
            words.add(simpleChallenge.getWord());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                words );
        wordsListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private StableArrayAdapter getAdapter(){
        this.challenges = (ListView) findViewById(org.ema.R.id.wordsListView);
        List<ChallengeSource> simpleChallenges = realm.where(ChallengeSource.class).findAll();
        for (ChallengeSource challenge : simpleChallenges){ // populates the words
            this.challengesList.add(challenge.getWord());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice,
                this.challengesList);

        this.challenges.setAdapter(adapter);
        this.challenges.setItemsCanFocus(false);
        this.challenges.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        return adapter;
    }


    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
