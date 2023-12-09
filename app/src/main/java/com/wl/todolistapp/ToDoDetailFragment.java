package com.wl.todolistapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ToDoDetailFragment extends Fragment {
    private long toDoId;
    private View rootView;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("toDoId", toDoId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            toDoId = savedInstanceState.getLong("toDoId");
        }
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        View view = getView();
//        if(view != null){
//            //now retrive data from database
//            ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
//            try {
//                SQLiteDatabase db = toDoDatabaseHelper.getReadableDatabase();
//                Cursor cursor = db.query ("ToDo",
//                        new String[] {"TITLE", "DESCRIPTION", "IS_COMPLETED"},
//                        "_id = ?",
//                        new String[] {Integer.toString((int) toDoId)},
//                        null, null,null);
//                //Move to the first record in the Cursor
//                if (cursor.moveToFirst()) {
//                    //Get the drink details from the cursor
//                    String titleText = cursor.getString(0);
//                    String descriptionText = cursor.getString(1);
//                    boolean isCompleted = (cursor.getInt(2) == 1);
//
//
//                    //title
//                    TextView title = (TextView) view.findViewById(R.id.taskTitle);
//                    title.setText(titleText);
//                    //description
//                    TextView description = (TextView) view.findViewById(R.id.taskDescription);
//                    description.setText(descriptionText);
//                    //switch status
//                    Switch switchStatus = (Switch) view.findViewById(R.id.taskStatusSwitch);
//                    //status
//                    TextView status = (TextView) view.findViewById(R.id.taskStatus);
//                    if(isCompleted){
//                        status.setText("Complete");
//                        switchStatus.setChecked(true);
//                    }else{
//                        status.setText("Incomplete");
//                        switchStatus.setChecked(false);
//                    }
//                }
//            } catch(SQLiteException e) {
//                Toast toast = Toast.makeText(this.getContext(), "Database unavailable", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//
//
//        }
//    }
@Override
public void onStart(){
    super.onStart();
    View view = getView();
    if(view != null){
        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = toDoDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("ToDo",
                    new String[] {"TITLE", "DESCRIPTION", "COMPLETE_STATUS"},
                    "_id = ?",
                    new String[] {Integer.toString((int) toDoId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String titleText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int completeStatus = cursor.getInt(2);

                TextView title = view.findViewById(R.id.taskTitle);
                title.setText(titleText);

                TextView description = view.findViewById(R.id.taskDescription);
                description.setText(descriptionText);

                Spinner statusSpinner = view.findViewById(R.id.taskStatusSpinner);
                statusSpinner.setSelection(completeStatus);
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast.makeText(this.getContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_to_do_detail, container, false);
        return rootView;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        Switch switchStatus = view.findViewById(R.id.taskStatusSwitch);
//        switchStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickChangeStatus(view);
//            }
//        });
//
//        Button deleteButton = view.findViewById(R.id.deleteTask);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickDeleteTask(view);
//            }
//        });
//
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner statusSpinner = view.findViewById(R.id.taskStatusSpinner);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onClickChangeStatus(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button deleteButton = view.findViewById(R.id.deleteTask);
        deleteButton.setOnClickListener(v -> onClickDeleteTask());
    }






//    public void onClickChangeStatus(View view) {
//        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
//        TextView title = (TextView) rootView.findViewById(R.id.taskTitle);
//        String taskName = title.getText().toString();
//
//        boolean isCompleted = ((Switch) view).isChecked();
//        toDoDatabaseHelper.updateToDoCompleted(taskName, isCompleted);
//
//        TextView taskStatus = (TextView) rootView.findViewById(R.id.taskStatus);
//        taskStatus.setText(isCompleted ? "Complete" : "Incomplete");
//    }
//
//
//    public void onClickDeleteTask(View view) {
//        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
//        TextView title = (TextView) rootView.findViewById(R.id.taskTitle);
//        String taskName = title.getText().toString();
//
//        toDoDatabaseHelper.deleteToDo(taskName);
//
//        // renew MainActivity
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//
//        // end current Activity
//        if (getActivity() != null) {
//            getActivity().finish();
//        }
//}
    public void onClickChangeStatus(int newStatus) {
        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
        TextView title = rootView.findViewById(R.id.taskTitle);
        String taskName = title.getText().toString();

        toDoDatabaseHelper.updateToDoCompleted(taskName, newStatus);
    }

    public void onClickDeleteTask() {
        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
        TextView title = rootView.findViewById(R.id.taskTitle);
        String taskName = title.getText().toString();

        toDoDatabaseHelper.deleteToDo(taskName);

        // renew MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // end current Activity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }




    public void setToDoId(long id){
        this.toDoId = id;
    }


}