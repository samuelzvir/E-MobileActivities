package br.com.samuelzvir.meuabc.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Student;

public class ProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
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
        List<Student> s = new Select().from(Student.class).queryList();
        ListView  students = (ListView) findViewById(R.id.studentslistView);

        List<String> studentNames = new ArrayList<>();
        for (Student tempStudent : s){ // populates the usernames
            studentNames.add(tempStudent.getNickname());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                studentNames );
        students.setAdapter(arrayAdapter);
    }

    public void toUserSettings(View view){
        Intent intent = new Intent(this,UserActivity.class);
        startActivity(intent);
    }

}
