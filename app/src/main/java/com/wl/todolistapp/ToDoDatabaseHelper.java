package com.wl.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ToDoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ToDo";
    private static final int DB_VERSION = 1;
    ToDoDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static void insertToDo(SQLiteDatabase db, String title, String description, int completeStatus, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String strDate = dateFormat.format(date);

        ContentValues todoValues = new ContentValues();
        todoValues.put("TITLE", title);
        todoValues.put("DESCRIPTION", description);
        todoValues.put("COMPLETE_STATUS", completeStatus);
        todoValues.put("INSERTION_TIME", strDate);

        db.insert("ToDo", null, todoValues);
    }



    public void updateToDoCompleted(String title, int completeStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COMPLETE_STATUS", completeStatus);

        db.update("ToDo", values, "TITLE = ?", new String[] { title });
    }

    public void deleteToDo(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ToDo", "TITLE = ?", new String[] { title });
    }




    private void updateMyDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ToDo (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TITLE TEXT, "
                + "DESCRIPTION TEXT, "
                + "COMPLETE_STATUS INTEGER, "
                + "INSERTION_TIME TEXT);");

        insertToDo(db, "My Office Worklist", "Complete the project report, attend the team meeting at 2 PM, and review the client feedback.", 0, new Date());
        insertToDo(db, "My Shopping List", "Buy milk, bread, and apples from the supermarket, and don't forget to check the pharmacy for vitamins.", 1, new Date());
        insertToDo(db, "My Studying List", "Read Chapter 4 of the history textbook, practice math problems, and prepare the science project outline.", 2, new Date());
    }
}
