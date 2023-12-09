package com.wl.todolistapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends ListFragment {
    static interface Listener {
        void itemClicked(long id);
    };

    public class ToDoItem {
        private long id;
        private String name;

        public ToDoItem(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private Listener listener;
    private boolean isSortedByName = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        Switch sortSwitch = (Switch) view.findViewById(R.id.switch_sort);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSortedByName = isChecked;
                updateUI();
            }
        });

        updateUI();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        ToDoItem item = (ToDoItem) getListAdapter().getItem(position);
        if (listener != null) {
            listener.itemClicked(item.getId());
        }
    }

    private List<ToDoItem> getToDoItemsFromDb() {
        List<ToDoItem> items = new ArrayList<>();
        ToDoDatabaseHelper toDoDatabaseHelper = new ToDoDatabaseHelper(getActivity());
        SQLiteDatabase db = toDoDatabaseHelper.getReadableDatabase();

        String sortOrder = isSortedByName ? "TITLE ASC" : "_id ASC";
        Cursor cursor = db.query("ToDo", new String[] {"_id", "TITLE"}, null, null, null, null, sortOrder);

        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex("_id");
            int titleColumnIndex = cursor.getColumnIndex("TITLE");
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumnIndex);
                String name = cursor.getString(titleColumnIndex);
                items.add(new ToDoItem(id, name));
            }
            cursor.close();
        } else {
            Toast toast = Toast.makeText(this.getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        return items;
    }

    public void updateUI() {
        List<ToDoItem> items = getToDoItemsFromDb();
        ArrayAdapter<ToDoItem> adapter = new ArrayAdapter<ToDoItem>(getActivity(), android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setText(getItem(position).getName());
                return view;
            }
        };
        setListAdapter(adapter);
    }
}
