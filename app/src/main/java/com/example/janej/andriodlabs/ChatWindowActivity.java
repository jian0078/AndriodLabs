package com.example.janej.andriodlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.janej.andriodlabs.StartActivity.ACTIVITY_NAME;

public class ChatWindowActivity extends Activity {
    ListView listView;
    EditText editText;
    Button button;
    ArrayList<String> messages;
    protected static final String ACTIVITY_NAME = "ChatWindow";
    ChatAdapter messageAdapter;
    Cursor cursor;
    ChatDatabaseHelper databaseHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        messageAdapter = new ChatAdapter(this);//Create a object to contain message
        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        messages = new ArrayList<>();
        listView.setAdapter(messageAdapter);

        databaseHelper =new ChatDatabaseHelper(this);
        db=databaseHelper.getWritableDatabase();
        cursor= db.query(false,ChatDatabaseHelper.TABLE_NAME,new String[]{ChatDatabaseHelper.KEY_ID,ChatDatabaseHelper.KEY_MESSAGE},null,
                null,null, null, null,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());// print an information message about the Cursor
            String message= cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)); //print out the name of each column returned by the cursor to retrieve each column name.
            messages.add(message);
            cursor.moveToNext();
        }
        for(int i=0; i< cursor.getColumnCount();i++){
            Log.i(ACTIVITY_NAME,"The column name is " +cursor.getColumnName(i));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.add(editText.getText().toString());
                ContentValues cv =new ContentValues();
                cv.put(ChatDatabaseHelper.KEY_MESSAGE,editText.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_NAME, null,cv);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
        databaseHelper.close();
    }

        private class ChatAdapter extends ArrayAdapter<String> {
            public ChatAdapter(Context ctx) {
                super(ctx, 0);
            }

            public int getCount() {
                return messages.size();
            }

            public String getItem(int position) {
                return messages.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();

            View result = null;
                if (position % 2 == 0) {
                    result = inflater.inflate(R.layout.chat_row_incoming, null);
                    TextView messageIn = result.findViewById(R.id.messageTextIn);
                    messageIn.setText(getItem(position)); // get the string at position
                } else {
                    result = inflater.inflate(R.layout.chat_row_outgoing, null);
                    TextView messageOut =  result.findViewById(R.id.messageTextOut);
                    messageOut.setText(getItem(position)); // get the string at position
                }

                return result;
            }
        }


    }
