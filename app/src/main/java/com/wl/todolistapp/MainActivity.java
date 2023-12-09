package com.wl.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements ToDoListFragment.Listener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void itemClicked(long id){
        View fragmentContainer = findViewById(R.id.fragment_container);
        if(fragmentContainer != null){
            //dynamically start fragment list (tablet)
            ToDoDetailFragment details = new ToDoDetailFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            details.setToDoId(id);
            ft.replace(R.id.fragment_container, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }else{
            //statically start fragment list (phone)
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_TODO_ID, (int) id);
            startActivity(intent);
        }

    }

    public void onClickAddTask(View view){

        EditText newTaskTitle = (EditText) findViewById(R.id.newTaskTitle);
        EditText newTaskDescription = (EditText) findViewById(R.id.newTaskDescription);
        String newTaskTitleText = newTaskTitle.getText().toString();
        String newTaskDescriptionText = newTaskDescription.getText().toString();
        int completeStatus = 0;
        Date date = new Date();

        //insert data into database
        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(this);
        SQLiteDatabase db = toDoDatabaseHelper.getWritableDatabase();
        ToDoDatabaseHelper.insertToDo(db, newTaskTitleText, newTaskDescriptionText, completeStatus, date);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.to_do_list_fragment);

        if (fragment instanceof ToDoListFragment) {
            ToDoListFragment toDoListFragment = (ToDoListFragment) fragment;
            toDoListFragment.updateUI();
        }
    }

    public void onClickSearchTask(View view){
        EditText searchTask = (EditText) findViewById(R.id.editText_search);
        String searchTaskName = searchTask.getText().toString();

        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(this);
        try{
            SQLiteDatabase db = toDoDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("ToDo",
                    new String[] {"_id"},
                    "TITLE LIKE ?",
                    new String[] {"%" + searchTaskName + "%"},
                    null, null,null);
            if (cursor.moveToFirst()){
                long id = cursor.getLong(0);
                itemClicked(id);
            }
            cursor.close();
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }


    }


}