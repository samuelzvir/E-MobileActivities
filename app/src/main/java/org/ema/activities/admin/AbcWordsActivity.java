package org.ema.activities.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ema.R;
import org.ema.entities.ChallengeSource;
import org.ema.entities.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

public class AbcWordsActivity  extends AppCompatActivity {
    private static final String TAG = "ABCActivity";
    final ArrayList<View> mCheckedViews = new ArrayList<View>();
    List<String> wordsList = new ArrayList<>();
    private Realm realm;
    private boolean ready = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_abc_user);
        List<ChallengeSource> simpleChallenges = realm.where(ChallengeSource.class).findAll(); //  new Select().from(ChallengeSource.class).queryList();
        final ListView words = (ListView) findViewById(org.ema.R.id.wordsListView);

        List<String> studentsNamesList = new ArrayList<>();

        for (ChallengeSource s : simpleChallenges){
            wordsList.add(s.getWord());
        }

        final AbcWordsActivity.StableArrayAdapter adapter = new AbcWordsActivity.StableArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice,
                studentsNamesList);

        mCheckedViews.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(org.ema.R.menu.menu_abc, menu);
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

    public void loadWords(View view){
        if(ready) {
            Log.d(TAG, "Saving game");

            final ListView users = (ListView) findViewById(org.ema.R.id.studentslistView);

            Object user = users.getAdapter().getItem(users.getCheckedItemPosition());
            Log.d(TAG, "Selected user: " + user.toString());

            final Student student = realm.where(Student.class).equalTo("nickname", user.toString()).findFirst();
            Intent intent = new Intent(this,CreateTextActivity.class);
            intent.putExtra("studentID", student.getId());
            startActivity(intent);
        }
    }

    public void toMenu(View view){
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
